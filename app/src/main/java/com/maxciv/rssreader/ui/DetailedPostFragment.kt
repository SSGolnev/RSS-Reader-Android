package com.maxciv.rssreader.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.maxciv.rssreader.R
import com.maxciv.rssreader.databinding.FragmentDetailedPostBinding
import com.maxciv.rssreader.viewmodels.DetailedPostViewModel

/**
 * @author maxim.oleynik
 * @since 06.03.2020
 */
class DetailedPostFragment : Fragment() {

    private lateinit var binding: FragmentDetailedPostBinding
    private val args: DetailedPostFragmentArgs by navArgs()
    private val viewModel: DetailedPostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setHabrPostAndParseImageUrl(args.habrPost)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_post, container, false)
        binding.lifecycleOwner = this
        binding.post = viewModel.habrPost

        viewModel.imageUrl?.let { imageUrl ->
            Glide.with(this)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imageView)
        }

        viewModel.habrPost.categories.forEach { category ->
            val chip = inflater.inflate(R.layout.chip_item_category, binding.categoriesChipGroup, false) as Chip
            chip.text = category
            binding.categoriesChipGroup.addView(chip)
        }

        binding.readMoreButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(viewModel.habrPost.link)
            })
        }

        return binding.root
    }
}
