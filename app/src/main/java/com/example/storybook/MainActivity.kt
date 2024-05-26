package com.example.storybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storybook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = Constants.getStoryList()
        setRecyclerViewAdapter(data)

    }

    private fun setRecyclerViewAdapter(data: ArrayList<Story>){

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.setHasFixedSize(true)
        val adapter = StoryAdapter(data)
        binding.recycler.adapter = adapter

        adapter.setOnClickListener(object: StoryAdapter.OnClickListener{
            override fun onClick(position: Int) {
                val intent = Intent(this@MainActivity, StoryActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)
            }
        })

    }

}