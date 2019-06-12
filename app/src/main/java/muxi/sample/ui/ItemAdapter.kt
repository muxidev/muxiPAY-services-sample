package muxi.sample.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_adapter.view.*
import muxi.sample.R

class ItemAdapter(private val context: Context,
                  private val itemAdapterOnClickHandler: ItemAdapterOnClickHandler)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    val mClickHandler: ItemAdapterOnClickHandler = itemAdapterOnClickHandler


    interface ItemAdapterOnClickHandler{
        fun onClick(type:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_adapter,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(position,mClickHandler)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val ecommerce_btn = itemView.ecommerce_btn

        fun bindView(position:Int, mClickHandler: ItemAdapterOnClickHandler){
            val text: String
            if(position == 0){
                text = "Present card"
            }else{
                text = "Ecommerce"
            }
            ecommerce_btn.setText(text)
            ecommerce_btn.setOnClickListener {
                Log.d("ItemAdapter","Onclick 2")

                mClickHandler.onClick(position)

            }
        }
    }
}