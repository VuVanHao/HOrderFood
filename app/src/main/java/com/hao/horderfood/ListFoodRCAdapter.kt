package com.hao.horderfood

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hao.horderfood.model.Food

class ListFoodRCAdapter( var food: ArrayList<Food>, val context: Context) : RecyclerView.Adapter<ListFoodRCAdapter.ListFoodViewHolder>(){

    private var activate:Boolean = false;
    private var position1:Int = 9999
    var mContext :Context? = null

    init {
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_list_mon_an,parent,false)
        return ListFoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListFoodViewHolder, position: Int) {
        val foodItem = food[position]
        holder.tvNameFoodItem?.text = foodItem.nameFood
        holder.tvPriceFoodItem?.text = foodItem.priceFood.toString()

        var img: ByteArray? = foodItem.imgFood
        var bitmap : Bitmap = BitmapFactory.decodeByteArray(img,0,img!!.size)

        if (activate && position1 == position)
        {
            holder.imgarrowtop?.visibility = View.VISIBLE
            holder.tvSuaFoodItem?.visibility = View.VISIBLE
            holder.tvXoaFoodItem?.visibility = View.VISIBLE
        }
        else
        {
            holder.imgarrowtop?.visibility = View.GONE
            holder.tvSuaFoodItem?.visibility = View.GONE
            holder.tvXoaFoodItem?.visibility = View.GONE
        }

        holder.imgFoodItem?.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return food.size
    }

    inner class ListFoodViewHolder( itemView : View ) : RecyclerView.ViewHolder(itemView)
    {
        var imgFoodItem :ImageView?= null
        var tvNameFoodItem :TextView?= null
        var tvPriceFoodItem :TextView?= null
        var imgarrowtop :ImageView?= null
        var tvSuaFoodItem :TextView?= null
        var tvXoaFoodItem :TextView?= null

        init {
            imgFoodItem = itemView.findViewById(R.id.imgFoodItem)
            tvNameFoodItem = itemView.findViewById(R.id.tvNameFoodItem)
            tvPriceFoodItem = itemView.findViewById(R.id.tvPriceFoodItem)
            tvSuaFoodItem = itemView.findViewById(R.id.btnSuaFoodItem)
            tvXoaFoodItem = itemView.findViewById(R.id.btnXoaFoodItem)
            imgarrowtop = itemView.findViewById(R.id.arrow_top)

            itemView.setOnLongClickListener(){
                val poss : Int = adapterPosition
                ( mContext as ActivityListMonAn).getItemClicked(poss)
                true
            }

            itemView.setOnClickListener{
                val poss : Int = adapterPosition
                ( mContext as ActivityListMonAn).clickItemListener(poss)
            }

            tvSuaFoodItem!!.setOnClickListener {
                val poss : Int = adapterPosition
                ( mContext as ActivityListMonAn).goToActivitySuaMonAn(poss)
            }

            tvXoaFoodItem!!.setOnClickListener {
                val poss : Int = adapterPosition
                ( mContext as ActivityListMonAn).deleteFoodItem(poss)
            }

        }
    }

    fun activateButtons(activate: Boolean, position:Int) {
        this.activate = activate
        this.position1 = position
        notifyDataSetChanged() //need to call it for the child views to be re-created with buttons.
    }
}