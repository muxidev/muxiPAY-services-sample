package muxi.kotlinsample.ui.present_card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import muxi.kotlinsample.R

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var debugItemList = mutableListOf<Item>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addItem(item: Item) {
        debugItemList.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.item_row_button,parent,false)
        return ItemViewHolder(rowView)
    }

    override fun getItemCount(): Int = debugItemList.toList().size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val debugItem = debugItemList.toList()[position]

        holder.button.text = debugItem.itemName
        holder.button.setOnClickListener(debugItem.onItemClicked)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button by lazy { itemView.findViewById<Button>(R.id.item_row_button) }
    }

    data class Item(val itemName : String, val onItemClicked: View.OnClickListener)
}