package android.example.com.productsinventory.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.example.com.productsinventory.activities.DetailActivity;
import android.example.com.productsinventory.activities.MainActivity;
import android.example.com.productsinventory.R;
import android.example.com.productsinventory.data.Product;
import android.example.com.productsinventory.utils.Constants;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ProductViewHolder> {

    List<Product> products;
    Context context;
    Resources res;

    public ProductsRecyclerAdapter(Context con, List<Product> products) {
        this.products = products;
        this.context = con;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView productInfo;
        TextView productQuantity;
        Button sellItem;
        ImageView ivProductImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            res =  context.getResources();
            cv = (CardView) itemView.findViewById(R.id.cvProduct);
            productInfo = (TextView) itemView.findViewById(R.id.tvProductInfo);
            productQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            sellItem = (Button) itemView.findViewById(R.id.btnSell);
            ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
        }
    }

    @Override
    public ProductsRecyclerAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductsRecyclerAdapter.ProductViewHolder holder, final int position) {

        holder.productInfo.setText(String.format(res.getString(R.string.app_message_productInfo), products.get(position).getName(), products.get(position).getPrice()));
        holder.productQuantity.setText(String.format(res.getString(R.string.app_message_productQty), products.get(position).getQuantity()));

        byte[] productImg = products.get(position).getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(productImg, 0, productImg.length);
        holder.ivProductImage.setImageBitmap(bmp);

        holder.cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constants.PRODUCT_ID_EXTRA, products.get(position).getId());
                context.startActivity(intent);
            }
        });

        if( products.get(position).getQuantity() != 0 ){
            holder.sellItem.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    final NumberPicker np = new NumberPicker(context);
                    np.setMinValue(1);
                    np.setMaxValue(products.get(position).getQuantity());
                    np.setValue(1);

                    alert.setTitle(res.getString(R.string.app_message_saleProduct_title))
                            .setPositiveButton(res.getString(R.string.app_message_sale), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    int pickedValue = np.getValue();
                                    int newQuantity = (products.get(position).getQuantity()) - pickedValue;
                                    int productId = (products.get(position).getId());
                                    ((MainActivity) context).updateProductQuantity(productId, newQuantity);
                                }
                            })
                            .setNegativeButton(res.getString(R.string.app_message_cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });

                    alert.setView(np);
                    alert.show();
                }
            });
        }
        else{
            holder.sellItem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if(products!= null){
            return products.size();
        }
        else
            return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void refreshList(List<Product> newList) {
        if (products != null) {
            products.clear();
            products.addAll(newList);
        } else {
            products = newList;
        }
        notifyDataSetChanged();
    }

}
