package com.example.sellayolanda.bookinventory;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sellayolanda.bookinventory.activity.Book;
import com.example.sellayolanda.bookinventory.activity.BookFormActivity;
import com.example.sellayolanda.bookinventory.adapter.BooksAdapter;
import com.example.sellayolanda.bookinventory.adapter.DividerDecoration;
import com.example.sellayolanda.bookinventory.helper.HelperFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public int TO_FORM = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerBook)
    RecyclerView recyclerBook;
    @BindView(R.id.fab)
    FloatingActionButton btnAdd;
    private List<Book> bookList = new ArrayList<Book>();
    private BooksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Books Catalog");

        mAdapter = new BooksAdapter(this, bookList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerBook.setLayoutManager(mLayoutManager);
        recyclerBook.setItemAnimator(new DefaultItemAnimator());
        recyclerBook.addItemDecoration(new DividerDecoration(this));

        recyclerBook.setAdapter(mAdapter);
        recyclerBook.addOnItemTouchListener(new HelperFunction.RecyclerTouchListener(this, recyclerBook, new HelperFunction.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(MainActivity.this, BookFormActivity.class);
                i.putExtra("bookEdit", bookList.get(position));
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, final int position) {
                final Book book = bookList.get(position);
                AlertDialog dialog = new AlertDialog.
                        Builder(MainActivity.this).setTitle("Delete")
                        .setMessage("Are you sure to delete " + book.getBook_title() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                bookList.remove(book);
                                mAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();
                dialog.show();
            }
        }));

        prepareBookData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BookFormActivity.class);
                startActivityForResult(i, TO_FORM);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) item.getActionView();

                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(MainActivity.this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TO_FORM) {
            Book bookForm = (Book) data.getExtras().getSerializable("book");
            bookList.add(bookForm);
            Toast.makeText(this, "Book " + bookForm.getBook_title() + " successfully added", Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void prepareBookData() {
        Book book = new Book("979-3062-92-4", "Sang Pemimpi", "Andrea Hirata", 2016, "Roman", "Sang Pemimpi adalah sebuah lantunan kisah kehidupan yang memesona dan akan membuat Anda percaya akan tenaga cinta, percaya pada kekuatan mimpi dan pengorbanan, lebih dari itu, akan membuat Anda percaya kepada Tuhan. Andrea akan membawa Anda berkelana menerobos sudut-sudut pemikiran di mana Anda akan menemukan pandangan yang berbeda tentang nasib, tantangan intelektualitas, dan kegembiraan yang meluap-luap, sekaligus kesedihan yang mengharu biru. \n" +
                "\n" +
                "Tampak komikal pada awalnya, selayaknya kenakalan remaja biasa, tapi kemudian tanpa Anda sadari, kisah dan karakter-karakter dalam buku ini lambat laun menguasai Anda. Karena potret-potret kecil yang menawan akan menghentakkan Anda pada rasa humor yang halus namun memiliki efek filosofis yang meresonansi. Karena arti perjuangan hidup dalam kemiskinan yang membelit dan cita-cita yang gagah berani dalam kisah dua orang tokoh utama buku ini: Arai dan Ikal akan menuntun Anda dengan semacam keanggunan dan daya tarik agar Anda dapat melihat ke dalam diri sendiri dengan penuh pengharapan, agar Anda menolak semua keputusasaan dan ketakberdayaan Anda sendiri. \n" +
                "\n" +
                "“Kita tak kan pernah mendahului nasib!” teriak Arai.\n" +
                "“Kita akan sekolah ke Prancis, menjelajahi Eropa sampai ke Afrika! Apa pun yang terjadi!”", R.drawable.sangpemimpi);
        bookList.add(book);

        book = new Book("978-979-22-5376-4", "Spring in London", "Ilana Tan", 2010, "Roman, Fiksi Remaja", "Astaga, ia—Danny Jo—adalah orang yang baik. Sungguh! Ia selalu bersikap ramah, sopan dan menyenangkan. Lalu kenapa Naomi Ishida menjauhinya seperti wabah penyakit? Bagaimana mereka bisa bekerja sama dalam pembuatan video musik ini kalau gadis itu mengacuhkannya setiap saat? Kesalahan apa yang sudah dia lakukan? \n" +
                "\n" +
                "Bagaimanapun juga Danny bukan orang yang gampang menyerah. Ia akan mencoba mendekati Naomi untuk mencari tahu alasan gadis itu memusuhinya. \n" +
                "\n" +
                "Tetapi ada dua hal yang tidak diperhitungkan Danny. Yang pertama adalah kemungkinan ia akan jatuh cinta pada Naomi Ishida yang dingin, misterius, dan penuh rahasia itu. Dan yang kedua adalah kemungkinan ia akan menguak rahasia gelap yang bisa menghancurkan mereka berdua dan orang-orang yang mereka sayangi.", R.drawable.spring);
        bookList.add(book);

        book = new Book("979-96257-0-X", "Supernova : Ksatria, Puteri, dan Bintang Jatuh", "Dewi Lestari", 2001, "Fiksi Ilmiah, Roman", "Dhimas dan Ruben adalah dua orang mahasiswa yang tengah menuntut ilmu di negeri Paman Sam. Dhimas kuliah di Goerge Washinton University, dan Ruben di John Hopkins Medical School. Mereka bertemu dalam suatu pesta yang meriah, yang diadakan oleh perkumpulan mahasiswa yang bersekolah di Amrik. Pertama kali bertemu mereke terlibat dalam percakapan yang saling menyudutkan satu samalain, hal tersebut dikarenakan oleh latar belakang mereka, Dhimas berasal dari kalangan The have, sedangkan Ruben, mahasiswa beasiswa. Tetapi setelah Ruben mencoba serotonin, mereka menjadi akrab membincangkan permasalahan iptek, saint, sampai acara buka-bukaan bahwa Ruben adalah seorang gay. Ternyata tak disangka-sangka bahwa Dhimas juga adalah seorang gay. Maka jadilah mereka sepasang kekasih, meskipun mereka tidak pernah serumah dalam satu apartemen. Bila ditanya mereka menjawab supaya bisa tetap kangen, tetap butuh usaha bila ingin bertemu satu sama lainnya. Dalam pertemuan di pesta tersebut mereka telah berikrar akan membuat satu karya. Satu masterpiece. Satu tulisan atau riset yang membantu menjembatani semua percabangan sains. Roman yang berdimensi luas dan mampu menggerakkan hati banyak orang.", R.drawable.supernova);
        bookList.add(book);

        book = new Book("9786022200406", "Yang Kedua", "Riawani Elyta", 2012, "Roman, Fiksi Remaja", "Kau bagai lagu indah, yang membuatku jatuh cinta dalam cara yang sederhana. Menyusup cepat ke dalam dada, mengentakkan hati, lalu mengisi penuh ruang kosong jiwa. Kaulah lirik yang selalu bisa kugumamkan berulang-ulang dalam benakku, tanpa jemu.\n" +
                "\n" +
                "Kau tahu, ketika aku menyerahkan hatiku—telah kuserahkan seluruhnya. Namun, mengapa kita tak bisa sekadar saling tatap. Mengapa kita tak bisa teriakkan bahwa cinta yang kita rasa membuat bahagia dalam diri luruh hingga ke ujung-ujung jari yang menghangat.\n" +
                "\n" +
                "Tak ada yang salah dengan cinta antara kau dan aku.\n" +
                "\n" +
                "Mungkin, yang salah hanyalah waktu. Kau dan aku, seharusnya sejak dulu bertemu…", R.drawable.yangkedua);
        bookList.add(book);

        mAdapter.notifyDataSetChanged();
    }
}

