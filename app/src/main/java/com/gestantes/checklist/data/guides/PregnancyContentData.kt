package com.gestantes.checklist.data.guides

/**
 * Conteúdos informativos e acolhedores por fase da gestação
 * 
 * NOTA: Conteúdo apenas informativo e acolhedor
 * NÃO é médico, NÃO é diagnóstico
 * 
 * Esta é uma adição ADITIVA - não altera dados existentes
 */

data class PregnancyContent(
    val id: String,
    val title: String,
    val emoji: String,
    val description: String,
    val sections: List<ContentSection>
)

data class ContentSection(
    val title: String,
    val emoji: String,
    val content: String,
    val tips: List<String> = emptyList()
)

data class TrimesterInfo(
    val trimester: Int,
    val title: String,
    val emoji: String,
    val weekRange: String,
    val description: String,
    val contents: List<PregnancyContent>
)

object PregnancyContentData {
    
    val trimesters = listOf(
        // PRIMEIRO TRIMESTRE
        TrimesterInfo(
            trimester = 1,
            title = "1º Trimestre",
            emoji = "🌱",
            weekRange = "Semanas 1 a 12",
            description = "O início da jornada - adaptação e formação",
            contents = listOf(
                PregnancyContent(
                    id = "1-body",
                    title = "Seu corpo está mudando",
                    emoji = "🦋",
                    description = "Entenda as transformações do primeiro trimestre",
                    sections = listOf(
                        ContentSection(
                            title = "O que esperar",
                            emoji = "💭",
                            content = "O primeiro trimestre é um período de grandes adaptações. Seu corpo está trabalhando intensamente para criar um ambiente perfeito para o bebê. É normal sentir cansaço, enjoos e mudanças de humor.",
                            tips = listOf(
                                "Descanse sempre que puder",
                                "Faça refeições pequenas e frequentes",
                                "Mantenha-se hidratada"
                            )
                        ),
                        ContentSection(
                            title = "Sintomas comuns",
                            emoji = "🤰",
                            content = "Enjoos matinais, cansaço, seios sensíveis, vontade frequente de urinar e mudanças de humor são completamente normais. Cada gestação é única!",
                            tips = listOf(
                                "Gengibre pode ajudar com enjoos",
                                "Evite alimentos muito gordurosos",
                                "Converse com seu médico sobre desconfortos"
                            )
                        )
                    )
                ),
                PregnancyContent(
                    id = "1-baby",
                    title = "Seu bebê está se formando",
                    emoji = "👶",
                    description = "A magia da formação inicial",
                    sections = listOf(
                        ContentSection(
                            title = "Desenvolvimento",
                            emoji = "✨",
                            content = "Nas primeiras 12 semanas, todos os órgãos principais do bebê começam a se formar. O coraçãozinho começa a bater por volta da 6ª semana. É um período mágico de crescimento!",
                            tips = listOf(
                                "O ácido fólico é essencial nessa fase",
                                "Evite álcool e cigarros",
                                "Mantenha uma alimentação equilibrada"
                            )
                        )
                    )
                ),
                PregnancyContent(
                    id = "1-emotions",
                    title = "Suas emoções importam",
                    emoji = "💕",
                    description = "Cuidando do emocional",
                    sections = listOf(
                        ContentSection(
                            title = "É normal sentir de tudo",
                            emoji = "🌈",
                            content = "Alegria, medo, ansiedade, emoção - todas essas emoções são válidas e normais. Os hormônios estão em alta e seu corpo está passando por grandes mudanças. Permita-se sentir.",
                            tips = listOf(
                                "Converse com pessoas de confiança",
                                "Não se cobre perfeição",
                                "Busque apoio se precisar"
                            )
                        )
                    )
                )
            )
        ),
        
        // SEGUNDO TRIMESTRE
        TrimesterInfo(
            trimester = 2,
            title = "2º Trimestre",
            emoji = "🌸",
            weekRange = "Semanas 13 a 27",
            description = "A lua de mel da gravidez",
            contents = listOf(
                PregnancyContent(
                    id = "2-body",
                    title = "Energia renovada",
                    emoji = "⚡",
                    description = "O trimestre mais confortável",
                    sections = listOf(
                        ContentSection(
                            title = "Bem-estar",
                            emoji = "😊",
                            content = "O segundo trimestre costuma ser o mais confortável. Os enjoos geralmente diminuem, a energia volta e você pode aproveitar mais a gravidez. A barriguinha começa a aparecer!",
                            tips = listOf(
                                "Aproveite para fazer exercícios leves",
                                "É um bom momento para viajar (com liberação médica)",
                                "Comece a preparar o enxoval"
                            )
                        ),
                        ContentSection(
                            title = "Mudanças no corpo",
                            emoji = "🤰",
                            content = "Sua barriga cresce, os seios aumentam e você pode perceber a linha nigra na barriga. Algumas mulheres sentem dores nas costas e podem aparecer estrias. Use cremes hidratantes!",
                            tips = listOf(
                                "Mantenha a pele hidratada",
                                "Use roupas confortáveis",
                                "Cuide da postura"
                            )
                        )
                    )
                ),
                PregnancyContent(
                    id = "2-baby",
                    title = "Sentindo seu bebê",
                    emoji = "💓",
                    description = "Os primeiros movimentos",
                    sections = listOf(
                        ContentSection(
                            title = "Chutinhos",
                            emoji = "👣",
                            content = "Entre a 16ª e 22ª semana, você pode começar a sentir os primeiros movimentos do bebê! No início parecem 'borboletas' na barriga. Com o tempo, ficam mais fortes e frequentes.",
                            tips = listOf(
                                "Cada bebê tem seu ritmo",
                                "Movimentos são sinal de que está tudo bem",
                                "Depois da 28ª semana, conte os movimentos diariamente"
                            )
                        )
                    )
                ),
                PregnancyContent(
                    id = "2-discover",
                    title = "Descobertas especiais",
                    emoji = "🎀",
                    description = "Momentos marcantes",
                    sections = listOf(
                        ContentSection(
                            title = "Sexo do bebê",
                            emoji = "💝",
                            content = "Por volta da 20ª semana, se você desejar, pode descobrir o sexo do bebê no ultrassom morfológico. É um momento emocionante! Mas lembre-se: o mais importante é a saúde.",
                            tips = listOf(
                                "Não há problema em querer surpresa",
                                "Chás revelação são opcionais",
                                "O importante é o bebê estar saudável"
                            )
                        )
                    )
                )
            )
        ),
        
        // TERCEIRO TRIMESTRE
        TrimesterInfo(
            trimester = 3,
            title = "3º Trimestre",
            emoji = "🍼",
            weekRange = "Semanas 28 a 40",
            description = "Preparação para o grande dia",
            contents = listOf(
                PregnancyContent(
                    id = "3-body",
                    title = "Reta final",
                    emoji = "🏁",
                    description = "Preparando para o parto",
                    sections = listOf(
                        ContentSection(
                            title = "Seu corpo se preparando",
                            emoji = "💪",
                            content = "No terceiro trimestre, seu corpo se prepara para o parto. Você pode sentir mais cansaço, dificuldade para dormir, azia e vontade frequente de ir ao banheiro. É a reta final!",
                            tips = listOf(
                                "Descanse com as pernas elevadas",
                                "Durma de lado (preferencialmente esquerdo)",
                                "Faça caminhadas leves"
                            )
                        ),
                        ContentSection(
                            title = "Contrações de treinamento",
                            emoji = "⏰",
                            content = "As contrações de Braxton Hicks são contrações de 'treinamento' que preparam seu útero. São irregulares e não indicam trabalho de parto. Se ficarem regulares e intensas, procure a maternidade.",
                            tips = listOf(
                                "Hidrate-se bastante",
                                "Mude de posição quando sentir",
                                "Se tiver dúvidas, sempre consulte seu médico"
                            )
                        )
                    )
                ),
                PregnancyContent(
                    id = "3-baby",
                    title = "Bebê quase pronto",
                    emoji = "👶",
                    description = "Últimos preparativos do bebê",
                    sections = listOf(
                        ContentSection(
                            title = "Desenvolvimento final",
                            emoji = "⭐",
                            content = "Seu bebê está ganhando peso, desenvolvendo os pulmões e se preparando para a vida fora do útero. Ele já escuta sua voz, reage à luz e tem períodos de sono e vigília.",
                            tips = listOf(
                                "Converse com seu bebê",
                                "Coloque músicas suaves",
                                "Observe os padrões de movimento"
                            )
                        )
                    )
                ),
                PregnancyContent(
                    id = "3-preparation",
                    title = "Preparativos finais",
                    emoji = "🧳",
                    description = "O que organizar",
                    sections = listOf(
                        ContentSection(
                            title = "Mala da maternidade",
                            emoji = "👜",
                            content = "A partir da 36ª semana, tenha sua mala pronta! Documentos, roupas para você e o bebê, itens de higiene. Deixe tudo organizado e fácil de pegar quando a hora chegar.",
                            tips = listOf(
                                "Tenha uma lista dos itens essenciais",
                                "Inclua carregador de celular",
                                "Leve roupas confortáveis para você"
                            )
                        ),
                        ContentSection(
                            title = "Sinais de trabalho de parto",
                            emoji = "🚗",
                            content = "Contrações regulares (a cada 5 minutos por 1 hora), perda do tampão mucoso, ruptura da bolsa. Na dúvida, sempre procure a maternidade. Confie no seu corpo!",
                            tips = listOf(
                                "Tenha o caminho da maternidade definido",
                                "Telefones de emergência à mão",
                                "Mantenha a calma - você vai conseguir!"
                            )
                        )
                    )
                ),
                PregnancyContent(
                    id = "3-emotional",
                    title = "Você está pronta",
                    emoji = "💕",
                    description = "Confiança e amor",
                    sections = listOf(
                        ContentSection(
                            title = "Confie em você",
                            emoji = "🌟",
                            content = "Você chegou até aqui! Cada passo dessa jornada te preparou para ser mãe. Confie no seu corpo, confie na sua intuição. Você vai ser uma mãe incrível!",
                            tips = listOf(
                                "Não existe mãe perfeita",
                                "Peça ajuda quando precisar",
                                "Cada bebê e cada mãe são únicos"
                            )
                        )
                    )
                )
            )
        )
    )
    
    /**
     * Retorna o trimestre baseado na semana atual
     */
    fun getTrimesterForWeek(week: Int): TrimesterInfo? {
        return when {
            week <= 12 -> trimesters.find { it.trimester == 1 }
            week <= 27 -> trimesters.find { it.trimester == 2 }
            else -> trimesters.find { it.trimester == 3 }
        }
    }
}

