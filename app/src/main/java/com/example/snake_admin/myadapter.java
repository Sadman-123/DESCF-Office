package com.example.snake_admin;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;
public class myadapter extends FirebaseRecyclerAdapter<Snake,myadapter.Myviewholder> {
    public myadapter(@NonNull FirebaseRecyclerOptions<Snake> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull Myviewholder holder, int position, @NonNull Snake model) {
        int itemCount = getItemCount();
        if (position < 0 || position >= itemCount) {
            return;
        }
        holder.snakeidtxt.setText("Snake ID: " + model.getId());
        holder.rescuebytxt.setText("Rescued By: " + model.getName());
        holder.authorisedbytxt.setText("Authorised: " + model.getAuth_name());
        holder.loctxt.setText("Location: " + model.getLoc());
        holder.timedatetxt.setText(model.getTime());
        Glide.with(holder.img.getContext()).load(model.getPhotourl()).into(holder.img);
        //if no image then show the default img
        if(model.getPhotourl().equals("")){
            holder.img.setImageResource(R.drawable.background);
        }
        holder.downbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("rescue/"+model.getRandomid());
                storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Set<String> downloadedFiles = new HashSet<>(); // Keep track of downloaded files
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url=uri.toString();
                                    Log.d("url",url);
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                    request.setDescription("Downloading");
                                    //make request settitle as filename
                                    //request.setTitle(uri.getLastPathSegment());
                                    String fileName = new File(uri.getPath()).getName();
                                    if(!downloadedFiles.contains(fileName)){
                                        request.setTitle(fileName);
                                        request.allowScanningByMediaScanner();
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Snake Rescue");
                                        DownloadManager manager = (DownloadManager) holder.img.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                        manager.enqueue(request);
                                        Toast.makeText(holder.img.getContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                                        downloadedFiles.add(fileName);
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors that occur while retrieving the list of files
                    }
                });
            }
        });
    }
    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new Myviewholder(view);
    }
    class Myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView snakeidtxt,rescuebytxt,authorisedbytxt,loctxt,timedatetxt;
        Button downbtn;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.snakeimgtxt);
            snakeidtxt = itemView.findViewById(R.id.snakeidtxt);
            rescuebytxt = itemView.findViewById(R.id.rescuebytxt);
            authorisedbytxt=itemView.findViewById(R.id.authorisedbytxt);
            loctxt=itemView.findViewById(R.id.loctxt);
            timedatetxt=itemView.findViewById(R.id.timedatetxt);
            downbtn=itemView.findViewById(R.id.downbtn);
        }
    }
}
