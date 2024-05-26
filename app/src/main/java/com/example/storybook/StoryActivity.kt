package com.example.storybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import com.example.storybook.databinding.ActivityStoryBinding
import java.util.Locale

class StoryActivity : AppCompatActivity(), OnInitListener {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyList: ArrayList<Story>
    private var position: Int = 0

    private var tts: TextToSpeech ?= null
    private val CHECK_TTS_DATA = 1
    private lateinit var speakableText: String

    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        position = intent.getIntExtra("position", 0)
        storyList = Constants.getStoryList()
        tts = TextToSpeech(this,this)

        // Check for TextToSpeech data using the intent
        val checkIntent = Intent()
        checkIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        startActivityForResult(checkIntent, CHECK_TTS_DATA)



        setStoryView()
        setSpeakableText()

        binding.nextButton.setOnClickListener {
            if (position < storyList.size-1){
                onChangeStory(1)
            }
            else{
                // position = 0 //Moves to first story
                // setStoryView()
                Toast.makeText(this, "Last Story", Toast.LENGTH_SHORT).show()
            }
        }

        binding.previousButton.setOnClickListener {
            if (position > 0){
                onChangeStory(-1)
            }
            else{
                // position = 9 //Moves to last story
                // setStoryView()
                Toast.makeText(this, "First Story", Toast.LENGTH_SHORT).show()
            }
        }

        binding.playButton.setOnClickListener {
            playStory()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHECK_TTS_DATA)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                Log.i("TTS", "TextToSpeech data is ready")
            }
            else
            {
                val installIntent = Intent()
                installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                startActivity(installIntent)
            }
        }

    }

    private fun setStoryView(){

        val story = storyList[position]
        binding.storyImage.setImageResource(story.image2)
        binding.tvStoryTitle.setText(story.title)
        binding.tvStory.setText(story.story)
        binding.tvMoral.setText(story.moral)

    }

    // TextToSpeech

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS)
        {
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS", "The specified language is not supported")
            }
        }
        else
        {
            Log.e("TTS", "Initialization failed!")
        }

        tts?.setOnUtteranceProgressListener(object: UtteranceProgressListener(){

            override fun onStart(utteranceId: String?) {
                runOnUiThread {
                    binding.playButton.setImageResource(R.drawable.pause)
                }
            }

            override fun onDone(utteranceId: String?) {
                runOnUiThread {
                    binding.playButton.setImageResource(R.drawable.play)
                }
            }

            override fun onError(p0: String?) {
                Log.e("TTS", "Error in speech synthesis")
            }

        })

    }

    private fun setSpeakableText()
    {
        speakableText = getString(storyList[position].story) + "So the Moral of the Story is " +
                    getString(storyList[position].moral)
    }

    private fun speakOut(text: String)
    {
        if (text.isNotEmpty())
        {
            if (tts?.isSpeaking == false) {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }

    private fun playStory(){
        if (!isPlaying)
        {
            isPlaying = true
            speakOut(speakableText)
            binding.playButton.setImageResource(R.drawable.pause)
        }
        else
        {
            tts?.stop()
            isPlaying = false
            binding.playButton.setImageResource(R.drawable.play)
        }
    }

    private fun onChangeStory(offset:Int)
    {
        isPlaying = false
        tts?.stop()
        position += offset
        setStoryView()
        binding.playButton.setImageResource(R.drawable.play)
        setSpeakableText()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (tts!=null){
            tts?.stop()
            tts?.shutdown()
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts!=null){
            tts?.stop()
            tts?.shutdown()
        }
    }
}