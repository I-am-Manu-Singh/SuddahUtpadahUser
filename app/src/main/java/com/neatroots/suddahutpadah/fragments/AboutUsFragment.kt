package com.neatroots.suddahutpadah.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neatroots.suddahutpadah.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {
    private val binding by lazy { FragmentAboutUsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aboutUsText = """
    <p>Welcome to <b>Suddah Utpadah </b>, your go-to partner for all your grocery delivery needs! We bring fresh groceries, vegetables, dairy products, and daily essentials right to your doorstep, making it easier for you to stock up on everything you need without leaving your home.</p><br>
    
    <h3>Our Mission:</h3>
    <p>At Suddah Utpadah , our mission is to provide a convenient and reliable shopping experience by delivering the highest-quality products directly to your home. We aim to simplify your daily life by offering a wide range of groceries and essentials with just a few clicks.</p><br>
    
    <h3>What We Offer:</h3>
    <div>
        <div><b>• Groceries:</b> From everyday staples like rice, flour, lentils, and spices to snacks and beverages, we ensure you have everything you need for a well-stocked kitchen.</div>
        <div><b>• Fresh Vegetables:</b> We deliver farm-fresh vegetables to your doorstep, helping you prepare healthy and delicious meals for your family.</div>
        <div><b>• Dairy Products:</b> Enjoy fresh dairy items such as milk, butter, and yogurt, sourced from trusted suppliers to guarantee quality and freshness.</div>
        <div><b>• Household Essentials:</b> Find a wide range of cleaning supplies, personal care products, and other household essentials in one place.</div>
    </div><br>
    
    <h3>Why Choose Suddah Utpadah :</h3>
    <div>
        <div><b>• Convenience:</b> Order from the comfort of your home and have your groceries delivered at your preferred time, saving you time and effort.</div>
        <div><b>• Freshness & Quality:</b> We prioritize delivering fresh, high-quality products to ensure you always receive the best.</div>
        <div><b>• Wide Range:</b> From kitchen staples to daily essentials, we offer a diverse range of products to meet all your household needs.</div>
        <div><b>• Customer Support:</b> Our friendly customer support team is always ready to assist you with any questions or concerns, ensuring a smooth shopping experience.</div>
    </div><br>
    
    <h3>Join Us:</h3>
    <p>Explore the Suddah Utpadah  app today and experience the convenience of getting your groceries delivered right to your doorstep. Whether you need fresh vegetables, pantry staples, or household essentials, we are here to make your shopping easy and hassle-free.</p><br>
    
    <p>Thank you for choosing Suddah Utpadah . We look forward to serving you and ensuring you never run out of the essentials that keep your home running smoothly!</p>
""".trimIndent()


        binding.tvAboutUs.text = Html.fromHtml(aboutUsText, Html.FROM_HTML_MODE_COMPACT)
    }



}