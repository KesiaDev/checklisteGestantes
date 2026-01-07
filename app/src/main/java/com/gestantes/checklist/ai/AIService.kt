package com.gestantes.checklist.ai

import android.content.Context
import com.gestantes.checklist.data.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Serviço de Inteligência Artificial
 * Responsável por análise emocional, respostas empáticas e busca inteligente
 */
class AIService(private val context: Context) {
    
    companion object {
        // Configurar sua API key aqui (em produção, usar de forma segura)
        private const val OPENAI_API_KEY = "" // Deixar vazio para usar respostas locais
        private const val OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"
        
        private const val SYSTEM_PROMPT = """
            Você é uma assistente virtual acolhedora e empática, especializada em apoio emocional para mães.
            
            REGRAS IMPORTANTES:
            1. NUNCA dê conselhos médicos ou substitua profissionais de saúde
            2. Use linguagem acolhedora, gentil e não julgadora
            3. Valide os sentimentos da mãe
            4. Ofereça apoio emocional e sugestões práticas do dia a dia
            5. Mantenha respostas concisas (máximo 3 parágrafos)
            6. Use emojis com moderação para tornar a mensagem mais calorosa
            7. Se identificar sinais de depressão pós-parto ou situação grave, sugira gentilmente buscar ajuda profissional
            
            Seu objetivo é fazer a mãe se sentir ouvida, apoiada e menos sozinha na jornada da maternidade.
        """
        
        @Volatile
        private var INSTANCE: AIService? = null
        
        fun getInstance(context: Context): AIService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AIService(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
    
    /**
     * Analisa uma entrada do diário e retorna uma resposta empática
     */
    suspend fun analyzeDiaryEntry(entry: DiaryEntry): AIResponse {
        return withContext(Dispatchers.IO) {
            if (OPENAI_API_KEY.isNotEmpty()) {
                analyzeWithOpenAI(entry)
            } else {
                analyzeLocally(entry)
            }
        }
    }
    
    /**
     * Responde a perguntas sobre documentos e registros
     */
    suspend fun answerQuestion(
        question: String,
        diaryEntries: List<DiaryEntry>,
        documents: List<BabyDocument>,
        medicalRecords: List<MedicalRecord>,
        developmentRecords: List<DevelopmentRecord>,
        growthRecords: List<GrowthRecord>
    ): AISearchResponse {
        return withContext(Dispatchers.IO) {
            searchIntelligently(question, diaryEntries, documents, medicalRecords, developmentRecords, growthRecords)
        }
    }
    
    /**
     * Análise usando OpenAI API (quando configurada)
     */
    private fun analyzeWithOpenAI(entry: DiaryEntry): AIResponse {
        return try {
            val url = URL(OPENAI_API_URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $OPENAI_API_KEY")
            connection.doOutput = true
            
            val prompt = buildDiaryAnalysisPrompt(entry)
            val requestBody = JSONObject().apply {
                put("model", "gpt-3.5-turbo")
                put("messages", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", SYSTEM_PROMPT)
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
                put("max_tokens", 500)
                put("temperature", 0.7)
            }
            
            connection.outputStream.bufferedWriter().use {
                it.write(requestBody.toString())
            }
            
            val response = connection.inputStream.bufferedReader().readText()
            val jsonResponse = JSONObject(response)
            val message = jsonResponse
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
            
            AIResponse(
                message = message,
                isFromAI = true,
                success = true
            )
        } catch (e: Exception) {
            analyzeLocally(entry)
        }
    }
    
    /**
     * Análise local (quando não há API configurada ou offline)
     * Usa respostas pré-definidas baseadas na emoção
     */
    private fun analyzeLocally(entry: DiaryEntry): AIResponse {
        val response = when (entry.emotion) {
            Emotion.HAPPY -> happyResponses.random()
            Emotion.GRATEFUL -> gratefulResponses.random()
            Emotion.CALM -> calmResponses.random()
            Emotion.TIRED -> tiredResponses.random()
            Emotion.ANXIOUS -> anxiousResponses.random()
            Emotion.INSECURE -> insecureResponses.random()
            Emotion.SAD -> sadResponses.random()
            Emotion.OVERWHELMED -> overwhelmedResponses.random()
            Emotion.HOPEFUL -> hopefulResponses.random()
            Emotion.LOVING -> lovingResponses.random()
        }
        
        return AIResponse(
            message = response,
            isFromAI = false,
            success = true
        )
    }
    
    /**
     * Busca inteligente em todos os dados
     */
    private fun searchIntelligently(
        question: String,
        diaryEntries: List<DiaryEntry>,
        documents: List<BabyDocument>,
        medicalRecords: List<MedicalRecord>,
        developmentRecords: List<DevelopmentRecord>,
        growthRecords: List<GrowthRecord>
    ): AISearchResponse {
        val results = mutableListOf<SearchResult>()
        val questionLower = question.lowercase()
        
        // Keywords para diferentes tipos de busca
        val documentKeywords = listOf("documento", "certidão", "vacina", "cartão", "receita", "exame", "onde está", "onde guardei")
        val diaryKeywords = listOf("escrevi", "senti", "quando", "dia", "diário", "memória")
        val medicalKeywords = listOf("consulta", "médico", "pediatra", "vacina", "doença", "remédio", "medicamento")
        val developmentKeywords = listOf("marco", "desenvolvimento", "primeiro", "primeira", "palavra", "passo", "dente")
        val growthKeywords = listOf("peso", "altura", "crescimento", "medida", "tamanho")
        
        // Buscar em documentos
        if (documentKeywords.any { questionLower.contains(it) } || documents.isNotEmpty()) {
            documents.filter { doc ->
                doc.title.lowercase().contains(questionLower) ||
                doc.description.lowercase().contains(questionLower) ||
                doc.tags.lowercase().contains(questionLower) ||
                doc.notes.lowercase().contains(questionLower) ||
                doc.documentType.displayName.lowercase().contains(questionLower)
            }.forEach { doc ->
                results.add(SearchResult(
                    type = "documento",
                    title = doc.title,
                    description = "📄 ${doc.documentType.displayName}",
                    date = doc.createdAt,
                    id = doc.id
                ))
            }
        }
        
        // Buscar no diário
        if (diaryKeywords.any { questionLower.contains(it) } || diaryEntries.isNotEmpty()) {
            diaryEntries.filter { entry ->
                entry.content.lowercase().contains(questionLower) ||
                entry.title.lowercase().contains(questionLower)
            }.take(5).forEach { entry ->
                results.add(SearchResult(
                    type = "diário",
                    title = entry.title.ifEmpty { "Entrada do diário" },
                    description = "📔 ${entry.emotion.emoji} ${entry.content.take(100)}...",
                    date = entry.createdAt,
                    id = entry.id
                ))
            }
        }
        
        // Buscar em registros médicos
        if (medicalKeywords.any { questionLower.contains(it) }) {
            medicalRecords.filter { record ->
                record.title.lowercase().contains(questionLower) ||
                record.description.lowercase().contains(questionLower) ||
                record.recordType.displayName.lowercase().contains(questionLower)
            }.forEach { record ->
                results.add(SearchResult(
                    type = "médico",
                    title = record.title,
                    description = "🏥 ${record.recordType.displayName}",
                    date = record.date,
                    id = record.id
                ))
            }
        }
        
        // Buscar em desenvolvimento
        if (developmentKeywords.any { questionLower.contains(it) }) {
            developmentRecords.filter { record ->
                record.title.lowercase().contains(questionLower) ||
                record.description.lowercase().contains(questionLower) ||
                record.milestoneType.displayName.lowercase().contains(questionLower)
            }.forEach { record ->
                results.add(SearchResult(
                    type = "desenvolvimento",
                    title = record.title,
                    description = "⭐ ${record.milestoneType.displayName}",
                    date = record.date,
                    id = record.id
                ))
            }
        }
        
        // Buscar em crescimento
        if (growthKeywords.any { questionLower.contains(it) }) {
            growthRecords.take(5).forEach { record ->
                val info = buildString {
                    record.weightKg?.let { append("Peso: ${it}kg ") }
                    record.heightCm?.let { append("Altura: ${it}cm") }
                }
                results.add(SearchResult(
                    type = "crescimento",
                    title = "Medição aos ${record.ageInMonths} meses",
                    description = "📏 $info",
                    date = record.date,
                    id = record.id
                ))
            }
        }
        
        val responseMessage = if (results.isEmpty()) {
            "Não encontrei informações sobre \"$question\". Tente usar outras palavras ou verifique se você já registrou essa informação."
        } else {
            "Encontrei ${results.size} resultado(s) para sua busca:"
        }
        
        return AISearchResponse(
            message = responseMessage,
            results = results.sortedByDescending { it.date }.take(10)
        )
    }
    
    private fun buildDiaryAnalysisPrompt(entry: DiaryEntry): String {
        return """
            A mãe escreveu no diário:
            
            Emoção selecionada: ${entry.emotion.displayName} ${entry.emotion.emoji}
            
            Texto:
            "${entry.content}"
            
            Por favor, ofereça uma resposta empática, acolhedora e encorajadora.
        """.trimIndent()
    }
    
    // ============ RESPOSTAS PRÉ-DEFINIDAS ============
    
    private val happyResponses = listOf(
        "Que alegria ler isso! 💕 Esses momentos de felicidade são preciosos. Guarde essa sensação no coração e lembre-se dela nos dias mais difíceis.",
        "Seu sorriso transparece através das palavras! 🌟 Aproveite cada segundo dessa felicidade. Você merece!",
        "Momentos assim fazem tudo valer a pena, não é? Continue celebrando as pequenas vitórias da maternidade! 🎉"
    )
    
    private val gratefulResponses = listOf(
        "A gratidão é um presente que damos a nós mesmas. 🙏 É lindo ver você reconhecendo as bênçãos na sua jornada.",
        "Que coração generoso você tem! Agradecer pelas pequenas coisas transforma nossa perspectiva. Continue assim! 💝",
        "A gratidão atrai mais motivos para agradecer. Você está no caminho certo, mamãe! 🌈"
    )
    
    private val calmResponses = listOf(
        "Que paz transmite suas palavras! 😌 Esses momentos de serenidade são fundamentais. Respire fundo e aproveite.",
        "A calma que você está sentindo é merecida. Você está fazendo um trabalho incrível como mãe. 🍃",
        "Momentos de tranquilidade na maternidade são como oásis. Desfrute dessa paz interior. 💫"
    )
    
    private val tiredResponses = listOf(
        "Eu entendo. O cansaço da maternidade é real e intenso. 💤 Lembre-se: descanse quando puder, aceite ajuda e seja gentil consigo mesma.",
        "Você está dando o seu melhor, mesmo exausta. Isso é amor em sua forma mais pura. 💕 Tente tirar alguns minutinhos só para você hoje.",
        "O cansaço vai passar, mas o amor que você está construindo é eterno. Cuide-se, mamãe. Você merece descanso. 🌙"
    )
    
    private val anxiousResponses = listOf(
        "A ansiedade faz parte da maternidade, mas não precisa dominar você. 🌸 Respire fundo: inspire contando até 4, segure por 4, expire por 4. Você consegue!",
        "É normal se preocupar - isso mostra o quanto você ama. Mas lembre-se: você é capaz e está fazendo um ótimo trabalho. 💪",
        "Uma coisa de cada vez, mamãe. Não tente resolver tudo agora. O que você pode fazer neste momento para se sentir melhor? 🤗"
    )
    
    private val insecureResponses = listOf(
        "Todas as mães têm dúvidas. Não existe manual perfeito, e você está aprendendo junto com seu bebê. 💕 Confie em seus instintos.",
        "A insegurança é sinal de que você se importa. Mas saiba: você conhece seu bebê melhor do que qualquer pessoa. 🌟",
        "Não existe mãe perfeita, existe mãe presente. E você está aqui, se dedicando. Isso é o que importa. 💝"
    )
    
    private val sadResponses = listOf(
        "Sinto muito que você está passando por um momento difícil. 💙 Seus sentimentos são válidos. Chorar faz bem, e pedir ajuda é sinal de força.",
        "Dias tristes fazem parte da jornada. Amanhã pode ser diferente. Por hoje, seja gentil consigo mesma. 🌧️➡️🌈",
        "Você não está sozinha nessa. Se a tristeza persistir, considere conversar com alguém de confiança ou um profissional. Cuidar de você é cuidar do seu bebê. 💕"
    )
    
    private val overwhelmedResponses = listOf(
        "Está tudo bem não dar conta de tudo. 🤍 A maternidade é intensa. O que você pode deixar de lado hoje? Priorize o essencial.",
        "Respire. Você não precisa ser perfeita. Peça ajuda, delegue o que puder, e lembre-se: isso também vai passar. 💪",
        "Quando tudo parece demais, faça uma coisa de cada vez. Apenas a próxima coisa. Você é mais forte do que imagina. 🌟"
    )
    
    private val hopefulResponses = listOf(
        "Que lindo sentir esperança! 🌟 Ela ilumina o caminho e nos dá forças para continuar. Continue acreditando!",
        "A esperança é a mãe de todas as virtudes. Seu otimismo é inspirador e contagiante! 🌈",
        "Com esperança no coração, tudo é possível. Seu bebê tem sorte de ter uma mãe tão positiva! 💕"
    )
    
    private val lovingResponses = listOf(
        "O amor transborda nas suas palavras! 💕 Esse vínculo que você está construindo é o maior presente que pode dar ao seu bebê.",
        "Amor de mãe é assim mesmo: infinito e incondicional. Que bonito ler sobre esse sentimento! 🥰",
        "Seu coração está cheio de amor, e seu bebê sente isso. Continue amando assim, com toda a sua alma! 💝"
    )
}

data class AIResponse(
    val message: String,
    val isFromAI: Boolean,
    val success: Boolean
)

data class AISearchResponse(
    val message: String,
    val results: List<SearchResult>
)

data class SearchResult(
    val type: String,
    val title: String,
    val description: String,
    val date: Long,
    val id: Long
)

