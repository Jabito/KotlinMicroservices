package com.jabito.microservices.serviceone.service

import org.springframework.stereotype.Service
import com.microsoft.cognitiveservices.speech.*
import com.microsoft.cognitiveservices.speech.audio.AudioConfig.fromWavFileInput
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClients
import javax.annotation.PostConstruct

@Service
class SpeechService{

    @PostConstruct
    fun listenToSpeech(){
        try {
            val speechSubscriptionKey = "274136944dab47c9a8542fe926ec5735"
            val serviceRegion = "southeastasia"

            val config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion)!!

            val reco = SpeechRecognizer(config)!!
            val commandListener = SpeechRecognizer(config)!!
            val phraseListGrammar = PhraseListGrammar.fromRecognizer(reco)
            val listenerGrammar = PhraseListGrammar.fromRecognizer(commandListener)
            var recording = false
            println("Listening 1 Open...")
            phraseListGrammar.addPhrase("add phrase list")
            phraseListGrammar.addPhrase("clear phrase list")
            listenerGrammar.addPhrase("start recording")
            listenerGrammar.addPhrase("stop recording")

            commandListener.recognized.addEventListener({s,e->
                if(e.getResult().getReason() === ResultReason.RecognizedSpeech) {
                    val phrase = e.result.text.toLowerCase()
                    if (phrase.contains("stop recording") && recording) {
                        recording = false
                        reco.stopContinuousRecognitionAsync().get()
                        println("---Stopping listener 2...---")
                    } else if (phrase.contains("start recording") && !recording) {
                        recording = true
                        reco.startContinuousRecognitionAsync().get()
                        println("---Starting listener 2...---")
                    }
                }
            })
            reco.recognized.addEventListener({ s, e ->
                if (e.getResult().getReason() === ResultReason.RecognizedSpeech) {
                    // Do something with the recognized text
                    println("---Listening...---")
                    val phrase = e.result.text.toLowerCase()
                     if(phrase.contains("add phrase list")){
                        addPhraseList(phraseListGrammar)
                        reco.stopContinuousRecognitionAsync().get()
                        reco.startContinuousRecognitionAsync().get()
                        println("---Added phrase list---")
                    }else if(phrase.contains("clear phrase list")){
                        clearPhraseList(phraseListGrammar)
                        reco.stopContinuousRecognitionAsync().get()
                        reco.startContinuousRecognitionAsync().get()
                        println("---Cleared phrase list---")
                    }else{
                        if(phrase.isNotEmpty()) {
                            println("Pushing to LUIS -> \"$phrase\"")
                            callLuisAPI(phrase)
                        }
                    }
                }
            })
            commandListener.startContinuousRecognitionAsync().get();
        } catch (ex: Exception) {
            println("Unexpected exception: " + ex.message)
            assert(false)
        }
    }

    private fun clearPhraseList(phraseListGrammar: PhraseListGrammar){
        phraseListGrammar.clear()
        phraseListGrammar.addPhrase("add phrase list")
        phraseListGrammar.addPhrase("clear phrase list")
        phraseListGrammar.addPhrase("start recording")
        phraseListGrammar.addPhrase("stop recording")
    }

    fun addPhraseList(phraseListGrammar: PhraseListGrammar){
        phraseListGrammar.addPhrase("Request a procedure")
        phraseListGrammar.addPhrase("get diagnosis")
        phraseListGrammar.addPhrase("go to CMG")
        phraseListGrammar.addPhrase("call HMO")
        phraseListGrammar.addPhrase("papa test")
        phraseListGrammar.addPhrase("papa consult")
    }

    fun addTerminologies(reco: SpeechRecognizer){

    }

    fun loadFromFile(){
        try {
            // Replace below with your own subscription key
            val speechSubscriptionKey = "274136944dab47c9a8542fe926ec5735"
            // Replace below with your own service region (e.g., "westus").
            val serviceRegion = "southeastasia"

            val config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion)!!

            val audioFile = "/Users/jabito/Work/MicroServices/KotlinMicroservices/service-one/20200212133421.wav"
            val audioInput = fromWavFileInput(audioFile)
            val reco = SpeechRecognizer(config, audioInput)!!

            val phraseListGrammar = PhraseListGrammar.fromRecognizer(reco)
            phraseListGrammar.addPhrase("request a procedure")
            phraseListGrammar.addPhrase("get diagnosis")
            phraseListGrammar.addPhrase("go to CMG")
            phraseListGrammar.addPhrase("your Konsole")

            val task = reco.recognizeOnceAsync()

            println("Listening...")
            val result = task.get()
            when (result.getReason()) {
                ResultReason.RecognizedSpeech -> {
                    System.out.println("We recognized: " + result.getText())
                }
                ResultReason.NoMatch -> println("NOMATCH: Speech could not be recognized.")
                ResultReason.Canceled -> {
                    val cancellation = CancellationDetails.fromResult(result)
                    System.out.println("CANCELED: Reason=" + cancellation.reason)

                    if (cancellation.reason === CancellationReason.Error) {
                        System.out.println("CANCELED: ErrorCode=" + cancellation.errorCode)
                        System.out.println("CANCELED: ErrorDetails=" + cancellation.errorDetails)
                        println("CANCELED: Did you update the subscription info?")
                    }
                }
            }

            reco.close()
        } catch (ex: Exception) {
            println("Unexpected exception: " + ex.message)
            assert(false)
        }
    }

    fun callLuisAPI(phrase: String){
        val httpclient = HttpClients.createDefault()

        try {

            // The ID of a public sample LUIS app that recognizes intents for turning on and off lights
            val AppId = "271eb839-42f0-44b9-bf32-f91fe98f110b"

            // Add your prediction Runtime key
            val Key = "f691cf920cd14a639613bba59898d5e5"

            // Add your endpoint, example is your-resource-name.api.cognitive.microsoft.com
            val Endpoint = "southeastasia.api.cognitive.microsoft.com"


            // Begin endpoint URL string building
            val endpointURLbuilder = URIBuilder("https://$Endpoint/luis/prediction/v3.0/apps/$AppId/slots/production/predict?")

            // query string params
            endpointURLbuilder.setParameter("query", phrase)
            endpointURLbuilder.setParameter("subscription-key", Key)
            endpointURLbuilder.setParameter("show-all-intents", "true")
            endpointURLbuilder.setParameter("verbose", "true")

            // create URL from string
            val endpointURL = endpointURLbuilder.build()

            // create HTTP object from URL
            val request = HttpGet(endpointURL)

            // access LUIS endpoint - analyze text
            val response = httpclient.execute(request)

            // get response
            val entity = response.getEntity()


            if (entity != null) {
                println(EntityUtils.toString(entity!!))
            }
        } catch (e: Exception) {
            println(e.message)
        }

    }
}
