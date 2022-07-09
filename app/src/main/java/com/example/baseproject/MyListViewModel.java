//package com.example.baseproject;
//
//import android.widget.ImageView;
//
//import androidx.databinding.BindingAdapter;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import java.util.ArrayList;
//
//public class MyListViewModel extends ViewModel {
//    public String id = "";
//    public String artistname = "";
//    public String artistimage = "";
//    public String moviename = "";
//    public MutableLiveData<ArrayList<MyListViewModel>> mutableLiveData = new MutableLiveData<>();
//    private ArrayList<MyListViewModel> arrayList;
//    private UserReposin userReposin;
//    private ArrayList<MyList> myList;
//
//    public MyListViewModel() {
//
//    }
//
//    public MyListViewModel(MyList myList) {
////        this.id = myList.id;
////        this.artistname = myList.artistname;
////        this.artistimage = myList.artistimage;
////        this.moviename = myList.moviename;
//    }
//
//    @BindingAdapter({"imageUrl"})
//    public static void loadimage(ImageView imageView, String imageUrl) {
////        Glide.with(imageView.getContext()).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(imageView);
//        //Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
//    }
//
//    public String getImageurl() {
//        return artistimage;
//    }
//
//    public MutableLiveData<ArrayList<MyListViewModel>> getMutableLiveData() {
//
//        arrayList = new ArrayList<>();
//
//        MyApi api = MyClient.getInstance().getMyApi();
//        Call<ArrayList<MyList>> call = api.getartistdata();
//        call.enqueue(new Callback<ArrayList<MyList>>() {
//            @Override
//            public void onResponse( Call<ArrayList<MyList>> call, Response<ArrayList<MyList>> response) {
//                myList = new ArrayList<>();
//                myList = response.body();
//                for (int i = 0; i < myList.size(); i++) {
//                    MyList myk = myList.get(i);
//                    MyListViewModel myListViewModel = new MyListViewModel(myk);
//                    arrayList.add(myListViewModel);
//                    mutableLiveData.setValue(arrayList);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<MyList>> call, Throwable t) {
//
//            }
//        });
//
//        return mutableLiveData;
//    }
//}
