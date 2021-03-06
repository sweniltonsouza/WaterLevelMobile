package br.com.coffeebeansdev.waterlevelmobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.coffeebeans.atividade.Atividade;
import br.com.coffeebeans.fachada.Fachada;

/**
 * Created by swenilton on 04/10/15.
 */
public class AtividadeListAdapter extends BaseAdapter {

    private List<Atividade> atividades;
    private Context context;
    private Fachada fachada;
    private FragmentManager fragmentManager;
    private DialogInterface dialogInterface;

    public AtividadeListAdapter(Context context, FragmentManager fragmentManager, List<Atividade> atividades) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.atividades = atividades;
        try {
            this.fachada = Fachada.getInstance(context);
        } catch (Exception e){
            Log.i("Fachada: ", e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return atividades.size();
    }

    @Override
    public Object getItem(int position) {
        return atividades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Atividade atividade = atividades.get(position);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.list_view_atividade, null);
        TextView textDesc = (TextView)view.findViewById(R.id.textDescricao);
        textDesc.setText(atividade.getDescricao());
        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnStop);
        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.btnEdit);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Tem certeza que deseja excluir o registro selecionado?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialogInterface = dialog;
                                try{
                                    new TaskAtividade(atividade.getId()).execute();
                                } catch (Exception e){
                                    Toast.makeText(view.getContext(), "Erro ao remover atividade\n" + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = fragmentManager;
                DialogInserirAtividade inserirAtividade = DialogInserirAtividade.newInstance("Atualizar Atividade", atividade.getId());
                inserirAtividade.show(fm, "fragment_inserir_atividade");
            }
        });
        return view;
    }

    private class TaskAtividade extends AsyncTask<Void, Void, String> {

        int id;

        public TaskAtividade(int id){
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            //showProgress(true);
            try {
                //fachada = Fachada.getInstance(getContext());
            } catch (Exception e) {
                Log.i("Erro Fachada", "Erro ao instancia fachada " + e.getMessage());
                Toast.makeText(context, "Erro ao instancia fachada\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                fachada.atividadeRemover(id);
            } catch (Exception e) {
                Log.i("Erro", "Erro ao remover atividade " + e.getMessage());
                Toast.makeText(context, "Erro dialog atividades\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(context, "Atividade removida.",
                    Toast.LENGTH_LONG).show();
            new FragmentAtividade().executeTask();
            dialogInterface.dismiss();
            //showProgress(false);
        }
    }

}
