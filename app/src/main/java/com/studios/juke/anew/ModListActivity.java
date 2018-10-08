package com.studios.juke.anew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studios.juke.anew.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Mods. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ModDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ModListActivity extends AppCompatActivity {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean twoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mod_list);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    if (findViewById(R.id.mod_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      twoPane = true;
    }

    View recyclerView = findViewById(R.id.mod_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this,
        DummyContent.ITEMS,
        twoPane));
  }

  public static class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final ModListActivity parentActivity;
    private final List<DummyContent.DummyItem> values;
    private final boolean twoPane;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
        if (twoPane) {
          Bundle arguments = new Bundle();
          arguments.putString(ModDetailFragment.ARG_ITEM_ID, item.id);
          ModDetailFragment fragment = new ModDetailFragment();
          fragment.setArguments(arguments);
          parentActivity.getSupportFragmentManager().beginTransaction()
              .replace(R.id.mod_detail_container, fragment)
              .commit();
        } else {
          Context context = view.getContext();
          Intent intent = new Intent(context, ModDetailActivity.class);
          intent.putExtra(ModDetailFragment.ARG_ITEM_ID, item.id);

          context.startActivity(intent);
        }
      }
    };

    SimpleItemRecyclerViewAdapter(ModListActivity parent,
                                  List<DummyContent.DummyItem> items,
                                  boolean twoPane) {
      this.values = items;
      this.parentActivity = parent;
      this.twoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.mod_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.idView.setText(values.get(position).id);
      holder.contentView.setText(values.get(position).content);

      holder.itemView.setTag(values.get(position));
      holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
      return values.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      final TextView idView;
      final TextView contentView;

      ViewHolder(View view) {
        super(view);
        idView = view.findViewById(R.id.id_text);
        contentView = view.findViewById(R.id.content);
      }
    }
  }
}
