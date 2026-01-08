package com.gestantes.checklist.ai

import kotlin.random.Random

/**
 * Sistema de IA Companheira
 * 
 * A "Lumi" é a assistente virtual carinhosa do app que acompanha
 * a gestante em toda sua jornada, oferecendo dicas, celebrações e apoio.
 */
object AICompanion {
    
    const val AI_NAME = "Lumi"
    const val AI_EMOJI = "✨"
    
    /**
     * Tipos de interação da IA
     */
    enum class InteractionType {
        GREETING,           // Saudação
        TIP,                // Dica útil
        ENCOURAGEMENT,      // Encorajamento
        CELEBRATION,        // Celebração de conquista
        SUGGESTION,         // Sugestão de ação
        COMFORT,            // Conforto emocional
        REMINDER,           // Lembrete gentil
        QUESTION,           // Pergunta reflexiva
        FUN_FACT            // Curiosidade sobre a gravidez
    }
    
    /**
     * Contextos onde a IA pode interagir
     */
    enum class Context {
        HOME,
        DIARY,
        CHECKLIST,
        WEEKLY_CHECKLIST,
        DOCUMENTS,
        GROWTH,
        HISTORY,
        BELLY_GALLERY,
        BABY_LETTER,
        BABY_SHOWER,
        CONTRACTION,
        REMINDER,
        TIMELINE
    }
    
    // ==================== SAUDAÇÕES ====================
    
    private val greetings = listOf(
        "Olá, mamãe! 💕 Como você está se sentindo hoje?",
        "Oi! Que bom te ver por aqui! $AI_EMOJI",
        "Bem-vinda de volta! Estou aqui para te ajudar! 🌸",
        "Olá! Pronta para mais um dia dessa jornada linda? 🌷",
        "Oi, mamãe querida! O que vamos fazer hoje? 💜"
    )
    
    private val morningGreetings = listOf(
        "Bom dia, mamãe! ☀️ Que seu dia seja leve e cheio de amor!",
        "Bom dia! Como foi a noite? Espero que tenha descansado bem! 🌅",
        "Bom dia, linda! Pronta para mais um dia especial? 🌻"
    )
    
    private val afternoonGreetings = listOf(
        "Boa tarde! Como está indo o seu dia? 🌤️",
        "Boa tarde, mamãe! Já fez uma pausinha hoje? ☕",
        "Boa tarde! Espero que esteja cuidando bem de você! 💕"
    )
    
    private val eveningGreetings = listOf(
        "Boa noite! Hora de relaxar um pouquinho? 🌙",
        "Boa noite, mamãe! Como foi seu dia? 💜",
        "Boa noite! Que tal uma água antes de descansar? 🌟"
    )
    
    fun getGreeting(hour: Int = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)): String {
        return when (hour) {
            in 5..11 -> morningGreetings.random()
            in 12..17 -> afternoonGreetings.random()
            else -> eveningGreetings.random()
        }
    }
    
    // ==================== DICAS POR CONTEXTO ====================
    
    fun getTip(context: Context, currentWeek: Int = 20): String {
        return when (context) {
            Context.HOME -> getHomeTip(currentWeek)
            Context.DIARY -> getDiaryTip()
            Context.CHECKLIST -> getChecklistTip()
            Context.WEEKLY_CHECKLIST -> getWeeklyChecklistTip(currentWeek)
            Context.DOCUMENTS -> getDocumentsTip()
            Context.GROWTH -> getGrowthTip(currentWeek)
            Context.HISTORY -> getHistoryTip()
            Context.BELLY_GALLERY -> getBellyGalleryTip(currentWeek)
            Context.BABY_LETTER -> getBabyLetterTip()
            Context.BABY_SHOWER -> getBabyShowerTip()
            Context.CONTRACTION -> getContractionTip()
            Context.REMINDER -> getReminderTip()
            Context.TIMELINE -> getTimelineTip(currentWeek)
        }
    }
    
    private fun getHomeTip(week: Int): String {
        val tips = when {
            week <= 12 -> listOf(
                "No primeiro trimestre, descanso é essencial! Não se cobre tanto. 💤",
                "Ácido fólico é super importante agora. Já tomou hoje? 💊",
                "Enjoos são normais. Tente comer porções menores várias vezes ao dia! 🍌",
                "Evite cheiros fortes se estiver com enjoo. É temporário! 🌸"
            )
            week <= 26 -> listOf(
                "O segundo trimestre costuma ser mais tranquilo. Aproveite! ✨",
                "Já sentiu o bebê mexer? É uma das sensações mais mágicas! 👶",
                "Ótima fase para organizar o quartinho e as coisinhas do bebê! 🍼",
                "Mantenha-se hidratada! Seu corpo precisa de muita água agora. 💧"
            )
            else -> listOf(
                "Reta final! O bebê está quase pronto para te conhecer! 🎉",
                "Já preparou a mala da maternidade? É hora de conferir! 👜",
                "Descanse bastante. Logo logo vocês se encontram! 💕",
                "Fique atenta aos movimentos do bebê. Ele já te conhece! 👶"
            )
        }
        return tips.random()
    }
    
    private fun getDiaryTip(): String {
        val tips = listOf(
            "Escrever sobre seus sentimentos ajuda a processar as emoções. 📝",
            "Que tal registrar como você está se sentindo hoje? 💭",
            "Seu diário será uma lembrança linda dessa fase! 📖",
            "Não precisa escrever muito. Às vezes uma frase já basta! ✨",
            "Registre também os momentos felizes. São memórias preciosas! 💕",
            "Seu bebê vai adorar ler isso um dia! 👶📚"
        )
        return tips.random()
    }
    
    private fun getChecklistTip(): String {
        val tips = listOf(
            "Não precisa fazer tudo de uma vez. Um item por dia já é ótimo! ✅",
            "Marque o que já fez e celebre cada conquista! 🎉",
            "Peça ajuda quando precisar. Você não está sozinha! 🤝",
            "Priorize os itens mais importantes primeiro. 📋",
            "Cada item marcado é um passo mais perto do bebê! 👣"
        )
        return tips.random()
    }
    
    private fun getWeeklyChecklistTip(week: Int): String {
        val tips = listOf(
            "Semana $week! Cada semana é uma conquista. Parabéns! 🌟",
            "Vamos ver o que temos para essa semana? 📋",
            "Complete no seu ritmo. Sem pressa, mamãe! 💕",
            "Cada item concluído te aproxima do grande dia! 🎯",
            "Você está indo muito bem! Continue assim! 💪"
        )
        return tips.random()
    }
    
    private fun getDocumentsTip(): String {
        val tips = listOf(
            "Manter os documentos organizados facilita muito na hora H! 📁",
            "Não esqueça de salvar o cartão do pré-natal! 💳",
            "Guarde os exames importantes aqui. Tudo em um só lugar! 🏥",
            "Documentos digitalizados são mais difíceis de perder! 📱",
            "Lembre de atualizar quando fizer novos exames! ✨"
        )
        return tips.random()
    }
    
    private fun getGrowthTip(week: Int): String {
        val tips = listOf(
            "Registrar o crescimento ajuda você e o médico a acompanhar! 📊",
            "Cada medida conta a história do desenvolvimento do bebê! 👶",
            "Não se compare com outras gestantes. Cada gravidez é única! 💕",
            "Na semana $week, seu bebê está crescendo muito! 🌱",
            "Anote após cada consulta para não esquecer! 📝"
        )
        return tips.random()
    }
    
    private fun getHistoryTip(): String {
        val tips = listOf(
            "Manter o histórico médico organizado é muito importante! 🏥",
            "Anote todas as consultas e vacinas aqui! 💉",
            "Isso vai ser útil para você e para os médicos! 👨‍⚕️",
            "Histórico completo = acompanhamento melhor! ✨",
            "Não esqueça de atualizar após cada consulta! 📋"
        )
        return tips.random()
    }
    
    private fun getBellyGalleryTip(week: Int): String {
        val tips = listOf(
            "Fotos da barriguinha são lembranças para a vida toda! 📸",
            "Semana $week! Que tal uma foto para marcar? 🤰",
            "Tente tirar fotos sempre no mesmo ângulo. Fica lindo ver a evolução! ✨",
            "Seu bebê vai amar ver essas fotos um dia! 👶💕",
            "Cada semana sua barriga conta uma nova história! 🌟"
        )
        return tips.random()
    }
    
    private fun getBabyLetterTip(): String {
        val tips = listOf(
            "Cartas para o bebê são presentes emocionantes! 💌",
            "Escreva o que está sentindo. Seu bebê vai amar ler! 📝",
            "Pode ser curta ou longa. O importante é o carinho! 💕",
            "Conte sobre o dia, sobre você, sobre seus sonhos... ✨",
            "Essas palavras serão tesouros para sempre! 💎"
        )
        return tips.random()
    }
    
    private fun getBabyShowerTip(): String {
        val tips = listOf(
            "Organize a lista de presentes com calma! 🎁",
            "Priorize os itens mais necessários primeiro! 📋",
            "Compartilhe a lista com quem vai te presentear! 💕",
            "Não esqueça dos itens básicos como fraldas! 👶",
            "Uma lista organizada evita presentes repetidos! ✨"
        )
        return tips.random()
    }
    
    private fun getContractionTip(): String {
        val tips = listOf(
            "Contrações de treinamento (Braxton Hicks) são normais! 💪",
            "Anote o horário e duração para mostrar ao médico! ⏱️",
            "Se forem regulares e intensas, entre em contato com seu médico! 🏥",
            "Respire fundo durante as contrações. Você consegue! 🌬️",
            "Fique calma e observe o padrão das contrações! 📊"
        )
        return tips.random()
    }
    
    private fun getReminderTip(): String {
        val tips = listOf(
            "Lembretes te ajudam a não esquecer nada importante! 🔔",
            "Configure alertas para consultas e exames! 📅",
            "Melhor anotar do que confiar só na memória! 📝",
            "Eu te ajudo a lembrar de tudo! ✨",
            "Organize seus compromissos com tranquilidade! 💕"
        )
        return tips.random()
    }
    
    private fun getTimelineTip(week: Int): String {
        val tips = listOf(
            "Sua linha do tempo é única e especial! 📆",
            "Semana $week de ${40 - week} semanas! Estamos na contagem! 🎯",
            "Cada marco é uma vitória a ser celebrada! 🏆",
            "Acompanhe o desenvolvimento do seu bebê semana a semana! 👶",
            "Que jornada linda você está vivendo! ✨"
        )
        return tips.random()
    }
    
    // ==================== CELEBRAÇÕES ====================
    
    fun getCelebration(achievement: String): String {
        val celebrations = listOf(
            "🎉 Parabéns! $achievement! Você é incrível!",
            "✨ Uau! $achievement! Estou muito orgulhosa de você!",
            "🌟 Que maravilha! $achievement! Continue assim!",
            "💕 Amei! $achievement! Você está arrasando!",
            "🎊 Isso aí! $achievement! Celebrando com você!"
        )
        return celebrations.random()
    }
    
    fun getChecklistCelebration(completedCount: Int, totalCount: Int): String {
        val percentage = (completedCount * 100) / totalCount
        return when {
            percentage == 100 -> "🎉 INCRÍVEL! Você completou TUDO! Estou super orgulhosa! 💕"
            percentage >= 75 -> "✨ Uau! Mais de 75% concluído! Você está arrasando! 🌟"
            percentage >= 50 -> "💪 Metade já foi! Continue assim, mamãe! 🎯"
            percentage >= 25 -> "🌱 Ótimo começo! Cada passo conta! 💕"
            completedCount == 1 -> "✅ Primeiro item feito! É assim que se começa! 🌟"
            else -> "💕 Continue no seu ritmo. Você está indo bem!"
        }
    }
    
    fun getWeekCelebration(week: Int): String {
        return when {
            week == 12 -> "🎉 Fim do primeiro trimestre! Parabéns, mamãe! O risco de complicações diminuiu muito! 💕"
            week == 20 -> "✨ Metade da gestação! Você está na metade do caminho para conhecer seu bebê! 🌟"
            week == 28 -> "💜 Terceiro trimestre começando! A reta final chegou! 🎊"
            week == 37 -> "👶 Seu bebê já é considerado a termo! Pode chegar a qualquer momento! 🎉"
            week == 40 -> "🎊 Semana 40! O grande dia está muito próximo! Força, mamãe! 💪"
            week % 4 == 0 -> "🌟 Mais um mês completo! Semana $week e contando! 💕"
            else -> "✨ Semana $week! Cada dia é uma conquista! 🌸"
        }
    }
    
    // ==================== ENCORAJAMENTOS ====================
    
    fun getEncouragement(): String {
        val encouragements = listOf(
            "Você está fazendo um trabalho incrível, mamãe! 💪",
            "Cada dia você fica mais perto de conhecer seu bebê! 👶",
            "Confie em você. Você nasceu para isso! 🌟",
            "Sua força é inspiradora! Continue assim! ✨",
            "Você não está sozinha. Estou aqui com você! 💕",
            "Lembre-se: você é mais forte do que imagina! 💜",
            "Seu bebê tem muita sorte de ter você! 🌸",
            "Respire fundo. Você consegue! 🌬️",
            "Celebre cada pequena vitória. Todas contam! 🎉",
            "Você está criando uma vida. Isso é mágico! ✨"
        )
        return encouragements.random()
    }
    
    // ==================== SUGESTÕES ====================
    
    fun getSuggestion(context: Context, userData: UserDataSimple? = null): String {
        return when (context) {
            Context.DIARY -> when {
                userData?.lastDiaryDays != null && userData.lastDiaryDays > 3 -> 
                    "📝 Faz ${userData.lastDiaryDays} dias que você não escreve no diário. Que tal registrar como está?"
                else -> "💭 Que tal escrever sobre como você está se sentindo hoje?"
            }
            Context.CHECKLIST -> when {
                userData?.pendingItems != null && userData.pendingItems > 5 ->
                    "📋 Você tem ${userData.pendingItems} itens pendentes. Vamos resolver alguns hoje?"
                else -> "✅ Que tal dar uma olhada nos seus checklists?"
            }
            Context.BELLY_GALLERY -> when {
                userData?.lastPhotoWeek != null && userData.currentWeek - userData.lastPhotoWeek >= 2 ->
                    "📸 Já faz umas semanas desde a última foto! Que tal uma nova?"
                else -> "🤰 Uma foto da barriguinha para marcar essa semana?"
            }
            else -> getEncouragement()
        }
    }
    
    // ==================== PERGUNTAS REFLEXIVAS ====================
    
    fun getReflectiveQuestion(): String {
        val questions = listOf(
            "Como você está se sentindo hoje, de verdade? 💭",
            "O que te fez sorrir hoje? 😊",
            "Você já descansou um pouquinho hoje? 💤",
            "Bebeu água suficiente? 💧",
            "Já conversou com seu bebê hoje? 👶",
            "O que você gostaria de fazer pelo bebê essa semana? 🌟",
            "Tem algo te preocupando que eu possa ajudar? 💕",
            "Qual momento da gravidez você mais gostou até agora? ✨",
            "Já pensou em um nome? 👶💭",
            "Como você imagina o primeiro encontro com seu bebê? 🥰"
        )
        return questions.random()
    }
    
    // ==================== CURIOSIDADES ====================
    
    fun getFunFact(week: Int): String {
        return when (week) {
            4 -> "Na semana 4, o coração do bebê começa a bater! 💓"
            8 -> "Na semana 8, o bebê já tem o tamanho de uma framboesa! 🫐"
            12 -> "Na semana 12, o bebê já pode fazer caretas! 😊"
            16 -> "Na semana 16, o bebê pode ouvir sua voz! 🗣️👶"
            20 -> "Na semana 20, você pode descobrir o sexo do bebê! 💙💗"
            24 -> "Na semana 24, o bebê tem ciclos de sono! 😴"
            28 -> "Na semana 28, o bebê pode abrir os olhos! 👀"
            32 -> "Na semana 32, o bebê está praticando a respiração! 🌬️"
            36 -> "Na semana 36, o bebê está se posicionando para o parto! 👶"
            40 -> "Na semana 40, o bebê está pronto para nascer! 🎉"
            else -> {
                val facts = listOf(
                    "Seu bebê já pode reconhecer sua voz! 🗣️",
                    "O bebê passa 95% do tempo dormindo dentro da barriga! 😴",
                    "Bebês no útero podem sonhar! 💭",
                    "O coração do bebê bate cerca de 150 vezes por minuto! 💓",
                    "Bebês desenvolvem suas impressões digitais na barriga! 👆"
                )
                facts.random()
            }
        }
    }
    
    // ==================== MENSAGEM CONTEXTUAL COMPLETA ====================
    
    /**
     * Gera uma mensagem da IA baseada no contexto e dados do usuário
     */
    fun getMessage(
        context: Context,
        type: InteractionType = InteractionType.TIP,
        currentWeek: Int = 20,
        userData: UserDataSimple? = null
    ): AIMessage {
        val content = when (type) {
            InteractionType.GREETING -> getGreeting()
            InteractionType.TIP -> getTip(context, currentWeek)
            InteractionType.ENCOURAGEMENT -> getEncouragement()
            InteractionType.CELEBRATION -> getCelebration("mais uma conquista")
            InteractionType.SUGGESTION -> getSuggestion(context, userData)
            InteractionType.COMFORT -> getEncouragement() // Usa encorajamento como conforto
            InteractionType.REMINDER -> getTip(context, currentWeek)
            InteractionType.QUESTION -> getReflectiveQuestion()
            InteractionType.FUN_FACT -> getFunFact(currentWeek)
        }
        
        return AIMessage(
            content = content,
            type = type,
            aiName = AI_NAME,
            aiEmoji = AI_EMOJI
        )
    }
}

/**
 * Dados simplificados do usuário para a IA
 */
data class UserDataSimple(
    val momName: String = "",
    val currentWeek: Int = 20,
    val pendingItems: Int? = null,
    val lastDiaryDays: Int? = null,
    val lastPhotoWeek: Int? = null
)

/**
 * Mensagem da IA
 */
data class AIMessage(
    val content: String,
    val type: AICompanion.InteractionType,
    val aiName: String,
    val aiEmoji: String
)
