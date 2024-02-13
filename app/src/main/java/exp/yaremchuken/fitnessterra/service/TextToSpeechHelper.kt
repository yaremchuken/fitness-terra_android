package exp.yaremchuken.fitnessterra.service

import android.app.Application
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import exp.yaremchuken.fitnessterra.AppSettings

class TextToSpeechHelper(app: Application): TextToSpeech.OnInitListener, UtteranceProgressListener() {

    private val tts: TextToSpeech

    private val texts: MutableList<String> = ArrayList()

    var hold: Boolean = false

    init {
        tts = TextToSpeech(app, this)
        tts.setOnUtteranceProgressListener(this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val lang = tts.setLanguage(AppSettings.locale())
            if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed!")
        }
    }

    fun speakOut(text: String) {
        if (hold) {
            return
        }
        if (!texts.contains(text)) {
            texts.add(text)
            if (texts.size == 1) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
                println(text)
            }
        }
    }

    fun stop() {
        tts.stop()
        texts.clear()
    }

    fun clear() {
        stop()
        hold = true
    }

    override fun onStart(utteranceId: String?) { }

    override fun onDone(utteranceId: String?) {
        if (utteranceId != null) {
            texts.remove(utteranceId)
            if (hold) {
                return
            }
            if (texts.isNotEmpty()) {
                tts.speak(texts[0], TextToSpeech.QUEUE_FLUSH, null, texts[0])
                println(texts[0])
            }
        }
    }

    @Deprecated("Deprecated in UtteranceProgressListener.class")
    override fun onError(utteranceId: String?) { }
}