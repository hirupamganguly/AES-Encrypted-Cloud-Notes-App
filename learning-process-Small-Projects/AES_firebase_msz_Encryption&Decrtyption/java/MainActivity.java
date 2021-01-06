package com.example.aesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    List<String> list;
    public Adapter(List<String> list){
        this.list=list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        //This class will hold references to all the components of the item and also to our data item.
        TextView textView;
        View view;
        public ViewHolder(@NonNull View itemView) {
            //In the constructor of the ViewHolder, we will find all the elements within the view
            // and link them to the variables with method FindViewById.
            super(itemView);
            view=itemView;
            textView=itemView.findViewById(R.id.show_msz); // where we want to show data- in layout
        }
    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialisation happens in the onCreateViewHolder method. It looks a little bit heavy on functions,
        // but what it does is, it makes a view (or group of views) with a given layout.
        //LayoutInflater is a class, that takes care of making views out of XML layout design.
        View v= (View)LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view_in_recycler_view,parent,false);
        // define layout to how we want to show data in Recycler view

        return new ViewHolder(v);
        // we return a new ViewHolder. RecyclerView will then reference ViewHolder for us and
        // take care of preparing the next ones to have a smooth scroll.
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        //link the current item with the viewHolder in other words we need to show appropriate data for the item.
        String s=list.get(position);
        holder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText editText;
    Button button;
    Adapter adapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_view);
        editText=findViewById(R.id.type_here_edit_text);
        button=findViewById(R.id.send_button);
        List<String> list= new ArrayList();

        adapter= new Adapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("MESSAGE_NODE");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    String listItem=snap.getValue().toString();
                    try {
                        String decryptedOutput=AES_Decryption(listItem);
                        list.add(decryptedOutput);
                        recyclerView.scrollToPosition(list.size()-1);
                        editText.setText("");
                        adapter.notifyDataSetChanged();
                    }
                    catch (NoSuchPaddingException e) { e.printStackTrace(); } catch (NoSuchAlgorithmException e) { e.printStackTrace(); } catch (InvalidKeyException e) { e.printStackTrace(); } catch (BadPaddingException e) { e.printStackTrace(); } catch (IllegalBlockSizeException e) { e.printStackTrace(); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // From here data goes to database:
                Date date=new Date();
                String currentDate=Long.toString(date.getTime());
                try {
                    databaseReference.child(currentDate).setValue(AES_Encryption(editText.getText().toString()));
                    adapter.notifyDataSetChanged();
                }
                catch (NoSuchAlgorithmException e) { e.printStackTrace(); } catch (NoSuchPaddingException e) { e.printStackTrace(); } catch (InvalidKeyException e) { e.printStackTrace(); } catch (BadPaddingException e) { e.printStackTrace(); } catch (IllegalBlockSizeException e) { e.printStackTrace(); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }

            }
        });
    }
    // AES Steps
    private byte[] secrectKey={9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    private SecretKeySpec secretKeySpec=new SecretKeySpec(secrectKey,"AES");//SecretKeySpec used to construct a SecretKey from a byte array,

    private String AES_Encryption(String textToEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] byteoftextToEncrypt=textToEncrypt.getBytes(); // convert input into bytes
        Cipher cipher=Cipher.getInstance("AES");//In order to create a Cipher object, the application calls the Cipher's getInstance method, with the name of a cryptographic algorithm
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        byte[] outputEncryptedText=cipher.doFinal(byteoftextToEncrypt);// this variable store the encryption result
        String outputEncryptedString=new String(outputEncryptedText,"ISO-8859-1");// convert byte array to string for returning purpose.
        return outputEncryptedString;
    }
    private String AES_Decryption(String textToDecrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] byteoftextToDecrept=textToDecrypt.getBytes("ISO-8859-1");
        Cipher Dcipher=Cipher.getInstance("AES");
        Dcipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        byte[] outputDecryptedText=Dcipher.doFinal(byteoftextToDecrept);
        String outputDecryptedString=new String(outputDecryptedText);
        return outputDecryptedString;
    }
}