package com.gestantes.checklist.data.guides

/**
 * Marcos de desenvolvimento infantil do nascimento até 4 anos.
 * Baseado em guidelines pediátricos atualizados.
 * 
 * IMPORTANTE: Cada criança se desenvolve no seu próprio ritmo.
 * Este guia é apenas uma referência geral e não substitui avaliação profissional.
 * Consulte o pediatra se tiver dúvidas sobre o desenvolvimento do seu bebê.
 */
object DevelopmentData {

    data class DevelopmentPhase(
        val ageRange: String,
        val ageInMonths: IntRange,
        val icon: String,
        val summary: String,
        val physical: List<String>,
        val cognitive: List<String>,
        val social: List<String>,
        val language: List<String>,
        val tips: List<String>,
        val alertSigns: List<String>
    )

    val phases = listOf(
        // PRIMEIRO ANO
        DevelopmentPhase(
            ageRange = "0-1 mês",
            ageInMonths = 0..1,
            icon = "👶",
            summary = "Adaptação ao mundo! O bebê está conhecendo sua nova casa e família.",
            physical = listOf(
                "Movimentos reflexos (sucção, preensão)",
                "Mantém mãos fechadas na maior parte do tempo",
                "Vira a cabeça para os lados quando de bruços",
                "Reflexo de Moro (susto) presente",
                "Perde peso nos primeiros dias e depois recupera"
            ),
            cognitive = listOf(
                "Reconhece a voz da mãe",
                "Enxerga a ~20-30cm de distância",
                "Prefere rostos humanos",
                "Distingue claro e escuro",
                "Responde a sons altos"
            ),
            social = listOf(
                "Acalma-se com o colo e voz familiar",
                "Início do vínculo com cuidadores",
                "Chora para comunicar necessidades",
                "Pode fixar o olhar brevemente"
            ),
            language = listOf(
                "Choro é a principal comunicação",
                "Faz sons guturais",
                "Reage a vozes familiares",
                "Acalma-se com sons ritmados"
            ),
            tips = listOf(
                "Pele a pele é essencial - faça sempre que puder!",
                "Converse e cante para o bebê",
                "Não se preocupe com 'mimar' - atenda sempre",
                "Descanse quando o bebê descansa"
            ),
            alertSigns = listOf(
                "Não reage a sons altos",
                "Não mama/suga adequadamente",
                "Corpo muito mole ou muito rígido",
                "Não faz contato visual brevemente"
            )
        ),
        DevelopmentPhase(
            ageRange = "1-2 meses",
            ageInMonths = 1..2,
            icon = "😊",
            summary = "Primeiros sorrisos! O bebê começa a interagir mais com o mundo.",
            physical = listOf(
                "Levanta brevemente a cabeça de bruços",
                "Movimentos mais suaves",
                "Mãos começam a abrir mais",
                "Consegue acompanhar objetos com os olhos",
                "Reflexos começam a diminuir"
            ),
            cognitive = listOf(
                "Reconhece rostos familiares",
                "Presta atenção a rostos",
                "Segue objetos em movimento",
                "Começa a mostrar preferências"
            ),
            social = listOf(
                "PRIMEIRO SORRISO SOCIAL! 🎉",
                "Responde a sorrisos",
                "Acalma-se com voz e toque familiar",
                "Começa a demonstrar prazer"
            ),
            language = listOf(
                "Faz sons de vogais ('ah', 'eh')",
                "Choros diferentes para fome/sono/desconforto",
                "Gorgoleja",
                "Reage quando falam com ele"
            ),
            tips = listOf(
                "Comemore o primeiro sorriso! É um marco lindo",
                "Faça caretas e sorria de volta",
                "Tempo de bruços: 3-5 min por vez",
                "Mostre objetos coloridos e contrastantes"
            ),
            alertSigns = listOf(
                "Não sorri até 2 meses",
                "Não segue objetos com os olhos",
                "Não reage a rostos",
                "Não faz sons"
            )
        ),
        DevelopmentPhase(
            ageRange = "2-3 meses",
            ageInMonths = 2..3,
            icon = "🎯",
            summary = "Descobrindo as mãozinhas! O bebê está mais alerta e curioso.",
            physical = listOf(
                "Sustenta a cabeça por mais tempo",
                "Descobre as próprias mãos e as observa",
                "Abre e fecha as mãos",
                "Tenta alcançar objetos",
                "De bruços, levanta cabeça e peito"
            ),
            cognitive = listOf(
                "Mais alerta e atento",
                "Reconhece objetos à distância",
                "Antecipa rotinas (ex: hora do banho)",
                "Demonstra preferência por pessoas conhecidas"
            ),
            social = listOf(
                "Sorri espontaneamente",
                "Gosta de brincar com pessoas",
                "Pode imitar algumas expressões",
                "Demonstra contentamento"
            ),
            language = listOf(
                "Balbucia sons",
                "'Conversa' quando falam com ele",
                "Sons mais variados",
                "Vira em direção aos sons"
            ),
            tips = listOf(
                "Coloque brinquedos coloridos ao alcance",
                "Cante músicas e faça rimas",
                "Narre o que você está fazendo",
                "Tempo de bruços: 10-15 min por dia (divididos)"
            ),
            alertSigns = listOf(
                "Não sustenta a cabeça quando apoiado",
                "Não sorri para pessoas",
                "Não acompanha objetos",
                "Não emite sons"
            )
        ),
        DevelopmentPhase(
            ageRange = "3-4 meses",
            ageInMonths = 3..4,
            icon = "🎪",
            summary = "Fase das descobertas! O bebê quer explorar tudo.",
            physical = listOf(
                "Sustenta bem a cabeça",
                "Rola de barriga para costas (alguns bebês)",
                "Leva objetos à boca",
                "Agarra brinquedos",
                "Empurra com os pés quando apoiado"
            ),
            cognitive = listOf(
                "Descobre causa e efeito",
                "Prefere brinquedos que fazem som",
                "Mais curioso sobre o ambiente",
                "Reconhece rostos de longe"
            ),
            social = listOf(
                "Ri alto pela primeira vez! 🎉",
                "Gosta de brincar e interagir",
                "Pode estranhar desconhecidos",
                "Expressa mais emoções"
            ),
            language = listOf(
                "Balbucios mais elaborados",
                "Imita alguns sons",
                "Expressa alegria e descontentamento",
                "Vira rapidamente para sons"
            ),
            tips = listOf(
                "Ofereça mordedores (dentição pode começar)",
                "Brinque de esconde-esconde simples",
                "Deixe explorar diferentes texturas",
                "Responda aos balbucios como conversa"
            ),
            alertSigns = listOf(
                "Não ri ou sorri",
                "Não tenta pegar objetos",
                "Não reage a sons",
                "Não sustenta a cabeça"
            )
        ),
        DevelopmentPhase(
            ageRange = "4-6 meses",
            ageInMonths = 4..6,
            icon = "🧸",
            summary = "Hora de rolar! O bebê está cada vez mais ativo.",
            physical = listOf(
                "Rola dos dois lados",
                "Senta com apoio",
                "Transfere objetos entre as mãos",
                "Primeiros dentes podem aparecer",
                "Começa a empurrar com os braços de bruços"
            ),
            cognitive = listOf(
                "Entende permanência do objeto (começo)",
                "Explora objetos de todas as formas",
                "Responde ao próprio nome",
                "Interesse pelo espelho"
            ),
            social = listOf(
                "Ansiedade com estranhos começa",
                "Muito apegado aos pais",
                "Gosta de brincadeiras repetitivas",
                "Demonstra mais emoções"
            ),
            language = listOf(
                "Balbucia sílabas ('ba', 'ma', 'da')",
                "Usa sons para chamar atenção",
                "Responde ao próprio nome",
                "Entende tons de voz"
            ),
            tips = listOf(
                "Introdução alimentar aos 6 meses!",
                "Crie ambiente seguro para rolar",
                "Continue com tempo de bruços",
                "Leia livros de figuras simples"
            ),
            alertSigns = listOf(
                "Não rola em nenhuma direção",
                "Não tenta pegar objetos",
                "Não responde a sons ou vozes",
                "Muito rígido ou muito mole"
            )
        ),
        DevelopmentPhase(
            ageRange = "6-9 meses",
            ageInMonths = 6..9,
            icon = "🏃",
            summary = "Exploradores! Engatinhando e descobrindo o mundo.",
            physical = listOf(
                "Senta sem apoio",
                "Engatinha (ou arrasta, rola, impulsiona)",
                "Fica em pé com apoio",
                "Pega objetos pequenos com os dedos",
                "Bate palmas"
            ),
            cognitive = listOf(
                "Entende 'não' (nem sempre obedece!)",
                "Procura objetos escondidos",
                "Explora causa e efeito",
                "Aponta para objetos interessantes"
            ),
            social = listOf(
                "Ansiedade de separação forte",
                "Muito apegado a cuidadores",
                "Estranha pessoas novas",
                "Brinca de esconde-esconde"
            ),
            language = listOf(
                "Balbucia como se conversasse",
                "Pode falar 'mama' ou 'papa' (sem significado ainda)",
                "Entende gestos simples",
                "Imita sons e gestos"
            ),
            tips = listOf(
                "Segurança em casa: tampa em tomadas, grades em escadas",
                "Ofereça variedade de alimentos",
                "Encoraje a exploração segura",
                "Brinque de esconde-esconde para trabalhar ansiedade"
            ),
            alertSigns = listOf(
                "Não senta com apoio",
                "Não balbucia",
                "Não transfere objetos entre mãos",
                "Não demonstra afeto por cuidadores"
            )
        ),
        DevelopmentPhase(
            ageRange = "9-12 meses",
            ageInMonths = 9..12,
            icon = "🎂",
            summary = "Quase 1 ano! Primeiros passos e primeiras palavras!",
            physical = listOf(
                "Fica em pé sozinho",
                "Pode dar os primeiros passos!",
                "Pinça fina (pega objetos pequenos)",
                "Solta objetos voluntariamente",
                "Sobe em móveis baixos"
            ),
            cognitive = listOf(
                "Entende comandos simples",
                "Imita ações",
                "Procura objetos escondidos",
                "Usa objetos corretamente (escova no cabelo)"
            ),
            social = listOf(
                "Dá tchau",
                "Joga beijinho",
                "Mostra objetos para compartilhar",
                "Brinca de imitação"
            ),
            language = listOf(
                "Primeiras palavras com significado! 🎉",
                "'Mama', 'papa', 'não', 'dá'",
                "Entende mais do que fala",
                "Usa gestos para se comunicar"
            ),
            tips = listOf(
                "Comemore cada conquista!",
                "Nomeie tudo ao redor",
                "Incentive a andar segurando nas mãos",
                "Leia livros interativos"
            ),
            alertSigns = listOf(
                "Não engatinha ou se locomove",
                "Não fala nenhuma palavra",
                "Não aponta",
                "Não entende comandos simples"
            )
        ),
        
        // SEGUNDO ANO
        DevelopmentPhase(
            ageRange = "12-18 meses",
            ageInMonths = 12..18,
            icon = "🚶",
            summary = "Andando e falando! Fase de muita energia e curiosidade.",
            physical = listOf(
                "Anda sozinho",
                "Sobe escadas com ajuda",
                "Come com colher (sujeira é normal!)",
                "Empilha 2-3 blocos",
                "Começa a correr (cambaleia)"
            ),
            cognitive = listOf(
                "Explora ativamente o ambiente",
                "Resolve problemas simples",
                "Imita tarefas domésticas",
                "Aponta para coisas que quer"
            ),
            social = listOf(
                "Brinca sozinho perto de outras crianças",
                "Demonstra posse ('meu!')",
                "Faz birras (início)",
                "Busca aprovação dos pais"
            ),
            language = listOf(
                "Fala 5-20 palavras",
                "Combina gestos com palavras",
                "Entende muito mais do que fala",
                "Segue instruções simples"
            ),
            tips = listOf(
                "Fase do 'não' começa - seja paciente",
                "Nomeie emoções: 'Você está bravo porque...'",
                "Deixe ajudar nas tarefas (mesmo que demore)",
                "Leia muito!"
            ),
            alertSigns = listOf(
                "Não anda aos 18 meses",
                "Não fala nenhuma palavra",
                "Perde habilidades que tinha",
                "Não aponta ou gesticula"
            )
        ),
        DevelopmentPhase(
            ageRange = "18-24 meses",
            ageInMonths = 18..24,
            icon = "💥",
            summary = "Terrível ou maravilhoso? Fase intensa de autonomia!",
            physical = listOf(
                "Corre",
                "Chuta bola",
                "Sobe escadas sozinho (degrau a degrau)",
                "Empilha 4-6 blocos",
                "Desenha rabiscos"
            ),
            cognitive = listOf(
                "Brincadeira simbólica (faz de conta)",
                "Classifica por formas e cores",
                "Completa frases de músicas/histórias",
                "Resolve quebra-cabeças simples"
            ),
            social = listOf(
                "Birras são comuns - fase de regulação",
                "Quer fazer tudo sozinho",
                "Dificuldade em dividir",
                "Imita muito os adultos"
            ),
            language = listOf(
                "Explosão do vocabulário! (50+ palavras)",
                "Combina 2 palavras ('qué água')",
                "Nomeia partes do corpo",
                "Faz perguntas"
            ),
            tips = listOf(
                "Birras são normais - valide o sentimento",
                "Ofereça escolhas limitadas",
                "Mantenha rotina previsível",
                "Seja modelo de como lidar com frustrações"
            ),
            alertSigns = listOf(
                "Não combina 2 palavras aos 2 anos",
                "Não brinca de faz de conta",
                "Não imita",
                "Não segue instruções simples"
            )
        ),
        
        // TERCEIRO ANO
        DevelopmentPhase(
            ageRange = "2-3 anos",
            ageInMonths = 24..36,
            icon = "🌈",
            summary = "Fase das perguntas! 'Por quê?' será a palavra favorita.",
            physical = listOf(
                "Pula com os dois pés",
                "Pedala triciclo",
                "Sobe e desce escadas alternando pés",
                "Desenha círculos",
                "Veste-se com ajuda"
            ),
            cognitive = listOf(
                "Fase dos 'porquês' infinitos",
                "Conta até 3",
                "Conhece algumas cores",
                "Brinca de faz de conta elaborado",
                "Entende conceito de 'dois'"
            ),
            social = listOf(
                "Começa a brincar COM outras crianças",
                "Demonstra empatia",
                "Pode dividir (com incentivo)",
                "Tem amigos preferidos"
            ),
            language = listOf(
                "Frases de 3-4 palavras",
                "Fala bem o suficiente para estranhos entenderem",
                "Conta histórias simples",
                "Usa pronomes (eu, você)"
            ),
            tips = listOf(
                "Responda aos 'porquês' com paciência",
                "Desfralde quando mostrar sinais de prontidão",
                "Incentive brincadeiras com outras crianças",
                "Leia histórias mais longas"
            ),
            alertSigns = listOf(
                "Fala difícil de entender",
                "Não faz frases",
                "Não brinca de faz de conta",
                "Não interage com outras crianças"
            )
        ),
        
        // QUARTO ANO
        DevelopmentPhase(
            ageRange = "3-4 anos",
            ageInMonths = 36..48,
            icon = "🎨",
            summary = "Criatividade a mil! Histórias, arte e imaginação florescem.",
            physical = listOf(
                "Corre, pula, sobe com confiança",
                "Pode andar de bicicleta com rodinhas",
                "Desenha pessoas (cabeça com pernas)",
                "Corta com tesoura sem ponta",
                "Veste-se sozinho (quase tudo)"
            ),
            cognitive = listOf(
                "Conta até 10 ou mais",
                "Conhece cores e formas",
                "Entende conceito de tempo (ontem, amanhã)",
                "Memória mais desenvolvida",
                "Resolve quebra-cabeças de 8-12 peças"
            ),
            social = listOf(
                "Brinca cooperativamente",
                "Tem amigos imaginários",
                "Entende regras de jogos simples",
                "Negocia durante brincadeiras",
                "Demonstra preferências de amizade"
            ),
            language = listOf(
                "Frases completas e complexas",
                "Conta histórias longas",
                "Faz perguntas 'como' e 'por que'",
                "Vocabulário extenso (1000+ palavras)",
                "Gramática quase correta"
            ),
            tips = listOf(
                "Incentive a criatividade (arte, música, histórias)",
                "Responda perguntas com paciência",
                "Estabeleça rotinas e limites claros",
                "Prepare para a escola",
                "Muita brincadeira ao ar livre!"
            ),
            alertSigns = listOf(
                "Fala muito difícil de entender",
                "Não consegue contar histórias simples",
                "Não brinca com outras crianças",
                "Não segue instruções de 3 passos",
                "Não demonstra interesse por outros"
            )
        )
    )

    // Categorias de habilidades
    enum class SkillCategory(val displayName: String, val icon: String) {
        PHYSICAL("Motor", "🏃"),
        COGNITIVE("Cognitivo", "🧠"),
        SOCIAL("Social", "👥"),
        LANGUAGE("Linguagem", "🗣️")
    }
}

