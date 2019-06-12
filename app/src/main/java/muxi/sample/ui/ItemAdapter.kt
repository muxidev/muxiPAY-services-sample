package muxi.sample.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_adapter.view.*
import muxi.sample.R

class ItemAdapter(private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_adapter,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text: String
        if(position == 0){
            text = "Present card"
        }else{
            text = "Ecommerce"
        }
        holder.bindView(text)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ecommerce_btn = itemView.ecommerce_btn

        fun bindView(text:String){
            ecommerce_btn.setText(text)
        }
    }
}