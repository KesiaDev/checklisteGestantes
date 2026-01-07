package com.gestantes.checklist.notification

/**
 * Mensagens de conforto e apoio para mães recém-chegadas à maternidade.
 * 
 * Essas mensagens foram criadas com carinho para ajudar as mamães
 * a passarem por essa fase tão intensa de mudanças hormonais e emocionais.
 */
object ComfortMessages {

    /**
     * Lista de mensagens de conforto para notificações diárias
     */
    val messages = listOf(
        // Validação emocional
        "Seus sentimentos são válidos. Chorar faz parte, e está tudo bem. 💕",
        "Você não precisa ser perfeita, só precisa estar presente. E você está! ✨",
        "É normal sentir-se sobrecarregada. Uma respiração de cada vez, mamãe. 🌸",
        "Seus hormônios estão em festa, mas isso vai se acalmar. Tenha paciência consigo. 💜",
        "Você está fazendo um trabalho incrível, mesmo quando não parece. 🌷",
        
        // Sobre a nova rotina
        "Cada dia é uma nova chance de aprender. Você e seu bebê estão aprendendo juntos. 🌻",
        "Não existe manual para ser mãe. Você está criando o seu próprio caminho. 📖",
        "A casa bagunçada pode esperar. O vínculo com seu bebê, não. 💝",
        "Pequenos progressos são grandes vitórias. Celebre cada um deles! 🎉",
        "Descansar quando o bebê dorme não é preguiça, é sabedoria. 😴",
        
        // Autocuidado
        "Lembre-se: você também precisa de cuidado. Beba água, coma bem. 💧",
        "Pedir ajuda é sinal de força, não de fraqueza. Você não precisa fazer tudo sozinha. 🤝",
        "Reserve alguns minutos para você hoje. Um banho tranquilo, um chá... 🍵",
        "Seu corpo fez algo extraordinário. Seja gentil com ele. 🌹",
        "Você merece descanso. Aceite ajuda quando oferecerem. 💐",
        
        // Conexão com o bebê
        "Seu bebê te escolheu. Vocês formam uma dupla perfeita. 👶💕",
        "O choro do bebê não significa que você está falhando. É a única forma dele se comunicar. 🗣️",
        "Cada abraço, cada olhar... você está construindo memórias lindas. 📸",
        "Seu cheiro, sua voz, seu toque... você é o mundo todo para seu bebê. 🌍",
        "Confie nos seus instintos. Você conhece seu bebê melhor do que ninguém. 💫",
        
        // Sobre os altos e baixos
        "Dias difíceis não duram para sempre. Amanhã será diferente. 🌅",
        "Está tudo bem ter dias em que você só quer chorar. Deixe as lágrimas virem. 💧",
        "A maternidade tem altos e baixos. Você está navegando lindamente. ⛵",
        "Sentir saudade da sua vida anterior é normal. Não significa que você ama menos seu bebê. 💭",
        "Você é mais forte do que imagina. Olhe o quanto já conquistou! 💪",
        
        // Sobre comparações
        "Não se compare com outras mães. Cada jornada é única. 🦋",
        "Redes sociais não mostram a realidade. Você está indo muito bem! 📱❌",
        "Seu jeito de maternar é especial. Não existe forma certa ou errada. 🌈",
        "Cada bebê é diferente. Confie no seu próprio ritmo. ⏰",
        "Você é a mãe que seu bebê precisa. Exatamente como você é. 💖",
        
        // Palavras de encorajamento
        "Respire fundo. Você está fazendo um trabalho maravilhoso. 🌬️",
        "Um passo de cada vez. Uma mamada de cada vez. Uma troca de cada vez. 👣",
        "Você é amada. Você é importante. Você é suficiente. 💕",
        "Este momento difícil vai passar. A alegria vai voltar. ☀️",
        "Você nasceu para ser mãe deste bebê. Acredite em você! 🌟",
        
        // Sobre pedir ajuda
        "Ligar para alguém e desabafar não é fraqueza. É cuidar de si. 📞",
        "Aceitar comida pronta da visita é autocuidado. Aceite de coração! 🍲",
        "Você não precisa dar conta de tudo. Delegue o que puder. 📋",
        "Uma rede de apoio faz toda a diferença. Cultive a sua. 🕸️💕",
        "Grupos de mães podem ajudar. Você não está sozinha nessa. 👩‍👩‍👧",
        
        // Mensagens especiais
        "Hoje é um novo dia cheio de possibilidades. Você consegue! 🌄",
        "Seu bebê não precisa de uma mãe perfeita, precisa de uma mãe feliz. 😊",
        "Os primeiros meses são os mais intensos. Fica mais fácil, prometo! 🌸",
        "Você está escrevendo a história mais bonita: a do amor maternal. 📚",
        "Olhe para trás e veja o quanto você já evoluiu como mãe. 🏆",
        
        // Baby blues e saúde mental
        "Se a tristeza persistir, converse com seu médico. Cuidar da mente é essencial. 🧠💜",
        "Baby blues é comum e passageiro. Você vai superar isso. 💙",
        "Não tenha vergonha de pedir ajuda profissional se precisar. É força! 🩺",
        "Sua saúde mental importa tanto quanto a física. Cuide-se. 🌻",
        "Você não está sozinha. Muitas mães passam pelo que você está passando. 🤗"
    )

    /**
     * Retorna uma mensagem aleatória da lista
     */
    fun getRandomMessage(): String {
        return messages.random()
    }

    /**
     * Retorna uma mensagem baseada no dia do ano (consistente por dia)
     */
    fun getDailyMessage(): String {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val index = dayOfYear % messages.size
        return messages[index]
    }

    /**
     * Títulos carinhosos para as notificações
     */
    val notificationTitles = listOf(
        "Oi, mamãe! 💕",
        "Uma mensagem para você 🌸",
        "Momento de carinho 💜",
        "Lembrete amoroso ✨",
        "Para você, mamãe 🌷",
        "Com carinho 💝",
        "Você é incrível! 🌟"
    )

    /**
     * Retorna um título aleatório para a notificação
     */
    fun getRandomTitle(): String {
        return notificationTitles.random()
    }
}

