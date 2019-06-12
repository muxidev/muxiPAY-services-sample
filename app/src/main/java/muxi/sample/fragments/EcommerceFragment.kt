package muxi.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import muxi.sample.R

class EcommerceFragment : Fragment() {

    companion object {
        fun newInstance():EcommerceFragment{
            return EcommerceFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_adapter,null)
    }
}