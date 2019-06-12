package muxi.sample.ui.present_card

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_adapter.view.*
import muxi.sample.Constants.ECOMMERCE_ID
import muxi.sample.Constants.PRESENT_CARD_ID
import muxi.sample.Constants.TYPES_SIZE
import muxi.sample.R

class ItemAdapter(private val context: Context,
                  itemAdapterOnClickHandler: ItemAdapterOnClickHandler
)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val mClickHandler: ItemAdapterOnClickHandler = itemAdapterOnClickHandler


    interface ItemAdapterOnClickHandler{
        fun onClick(type:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_adapter,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return TYPES_SIZE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(position,mClickHandler)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val ecommerceBtn = itemView.btn_item!!

        fun bindView(position:Int, mClickHandler: ItemAdapterOnClickHandler){
            val text: String = when (position) {
                PRESENT_CARD_ID -> itemView.context.getString(R.string.present_card)
                ECOMMERCE_ID -> itemView.context.getString(R.string.ecommerce)
                else -> itemView.context.getString(R.string.something_wrong)
            }
            ecommerceBtn.text = text
            ecommerceBtn.setOnClickListener {
                mClickHandler.onClick(position)

            }
        }
    }
}