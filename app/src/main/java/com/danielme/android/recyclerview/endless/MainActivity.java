package com.danielme.android.recyclerview.endless;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danielme.android.recyclerview.endless.model.Color;
import com.danielme.android.recyclerview.endless.model.Footer;
import com.danielme.android.recyclerview.endless.model.ItemRv;
import com.danielme.android.recyclerview.endless.rv.EndlessScrollListener;
import com.danielme.android.recyclerview.endless.rv.MaterialPaletteAdapter;
import com.danielme.android.recyclerview.endless.rv.RecyclerViewOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author danielme.com
 */
public class MainActivity extends AppCompatActivity implements EndlessScrollListener.ScrollerClient {

  private static final int MAX_LIST_ELEMENTS = 35;

  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private Button retryButton;

  private boolean forceError;

  private List<ItemRv> itemRvs;
  private AsyncTask<Void, Void, Void> asyncTask;
  private EndlessScrollListener endlessScrollListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    initAttributes();
    setupRecyclerView();
    launchAsynTask();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (asyncTask != null) {
      asyncTask.cancel(false);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.activity_main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    forceError = !forceError;
    Toast.makeText(this, "error " + forceError, Toast.LENGTH_SHORT).show();
    return true;
  }

  private void initAttributes() {
    recyclerView = findViewById(R.id.recyclerView);
    progressBar = findViewById(R.id.progressBar);
    retryButton = findViewById(R.id.retryButton);
    itemRvs = new ArrayList<>();
  }

  private void setupRecyclerView() {
    recyclerView.setAdapter(new MaterialPaletteAdapter(itemRvs, new
            RecyclerViewOnItemClickListener() {
              @Override
              public void onClick(View v, int position) {
                if (itemRvs.get(position) instanceof Color) {
                  String text = position + " " + ((Color) itemRvs.get(position)).getName();
                  Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                }
              }
            }));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
            ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
    recyclerView.addItemDecoration(dividerItemDecoration);

    endlessScrollListener = new EndlessScrollListener(this);
    recyclerView.addOnScrollListener(endlessScrollListener);
  }

  private void launchAsynTask() {
    asyncTask = new LoadColorsAsyncTask(this);
    asyncTask.execute();
  }

  @Override
  public void loadData() {
    launchAsynTask();
  }

  public void displayLoadingIndicator() {
    //no hay datos, la lista está vacía, mostrar loading en el centro
    if (itemRvs.isEmpty()) {
      retryButton.setVisibility(View.GONE);
      progressBar.setVisibility(View.VISIBLE);
    } else {
      //si ya se muestran datos, añadimos el footer al final
      itemRvs.add(new Footer());
      recyclerView.getAdapter().notifyItemInserted(itemRvs.size() - 1);
    }
  }

  public void displayData(List<Color> colors) {
    progressBar.setVisibility(View.GONE);
    removeFooter();
    itemRvs.addAll(colors);
    recyclerView.getAdapter().notifyItemRangeChanged(itemRvs.size() - 1, colors.size());
    if (itemRvs.size() > MAX_LIST_ELEMENTS) {
      Toast.makeText(MainActivity.this, getString(R.string.end), Toast.LENGTH_SHORT).show();
    } else {
      endlessScrollListener.loadMore();
    }
  }

  private void removeFooter() {
    int size = itemRvs.size();
    if (!itemRvs.isEmpty() && itemRvs.get(size - 1) instanceof Footer) {
      itemRvs.remove(size - 1);
      recyclerView.getAdapter().notifyItemRemoved(size - 1);
    }
  }

  public void onError() {
    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    progressBar.setVisibility(View.GONE);
    //si ha fallado y la lista está vacia se muestra el botón.
    if (recyclerView.getAdapter().getItemCount() == 0) {
      retryButton.setVisibility(View.VISIBLE);
    } else {
      removeFooter();
      //si ya hay elementos, las nuevas cargas de datos se realizarán mediante scroll
      //el scroll no se debloquea hasta que se hayan renderizado todos los cambios en pantalla
      //para evitar que si se está mostrando actualmente el footer, al eliminarse se haga scroll automático
      //y pueda entrar en un bucle infinito
      final Handler handlerUI = new Handler(Looper.getMainLooper());
      Runnable r = new Runnable() {
        public void run() {
          endlessScrollListener.loadMore();
        }
      };
      handlerUI.post(r);
    }
  }

  public boolean forceError() {
    return forceError;
  }

  public void retry(View view) {
    launchAsynTask();
  }

}