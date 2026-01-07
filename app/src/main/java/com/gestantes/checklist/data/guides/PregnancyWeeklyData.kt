package com.gestantes.checklist.data.guides

/**
 * Dados dos checklists semanais da gestação
 * Organizado por semanas-chave: 8, 12, 20, 28, 32, 36, 38, 40
 * 
 * NOTA: Este arquivo é ADITIVO - não altera os checklists existentes
 */

data class WeeklyChecklist(
    val week: Int,
    val title: String,
    val description: String,
    val emoji: String,
    val items: List<String>,
    val tips: List<String> = emptyList()
)

object PregnancyWeeklyData {
    
    val weeklyChecklists = listOf(
        WeeklyChecklist(
            week = 8,
            title = "Semana 8",
            description = "Primeiras consultas e exames",
            emoji = "🌱",
            items = listOf(
                "Agendar primeira consulta de pré-natal",
                "Iniciar ácido fólico (se ainda não começou)",
                "Fazer exames de sangue iniciais",
                "Verificar vacinas em dia",
                "Começar a evitar alimentos crus",
                "Reduzir consumo de cafeína",
                "Informar médico sobre medicamentos em uso",
                "Pesquisar sobre plano de saúde/maternidade"
            ),
            tips = listOf(
                "É normal sentir enjoos nessa fase",
                "Descanse sempre que possível",
                "Mantenha-se hidratada"
            )
        ),
        WeeklyChecklist(
            week = 12,
            title = "Semana 12",
            description = "Final do primeiro trimestre",
            emoji = "🍀",
            items = listOf(
                "Realizar ultrassom morfológico de 1º trimestre",
                "Fazer exame de translucência nucal",
                "Atualizar cartão de vacinas",
                "Começar a usar roupas mais confortáveis",
                "Informar família e amigos (se desejar)",
                "Pesquisar sobre tipos de parto",
                "Iniciar exercícios leves (com liberação médica)",
                "Organizar documentos do pré-natal"
            ),
            tips = listOf(
                "Os enjoos costumam diminuir a partir dessa semana",
                "O risco de perda gestacional diminui significativamente"
            )
        ),
        WeeklyChecklist(
            week = 20,
            title = "Semana 20",
            description = "Metade da gestação!",
            emoji = "🎀",
            items = listOf(
                "Realizar ultrassom morfológico de 2º trimestre",
                "Descobrir o sexo do bebê (se desejar)",
                "Começar a montar o enxoval",
                "Pesquisar sobre cursos para gestantes",
                "Verificar licença-maternidade",
                "Começar a preparar o quarto do bebê",
                "Fazer lista de presentes (se for fazer chá de bebê)",
                "Agendar próximas consultas do pré-natal"
            ),
            tips = listOf(
                "Você pode começar a sentir o bebê mexer!",
                "A barriga começa a ficar mais evidente"
            )
        ),
        WeeklyChecklist(
            week = 28,
            title = "Semana 28",
            description = "Início do terceiro trimestre",
            emoji = "🌟",
            items = listOf(
                "Fazer exame de curva glicêmica",
                "Tomar vacina contra coqueluche (dTpa)",
                "Verificar hemograma e ferritina",
                "Começar a contar movimentos do bebê",
                "Pesquisar sobre amamentação",
                "Visitar maternidade escolhida",
                "Organizar mala da maternidade (começar)",
                "Fazer curso de gestantes (se disponível)"
            ),
            tips = listOf(
                "O bebê já consegue abrir os olhos",
                "Comece a descansar mais"
            )
        ),
        WeeklyChecklist(
            week = 32,
            title = "Semana 32",
            description = "Reta final se aproximando",
            emoji = "🎈",
            items = listOf(
                "Realizar ultrassom de crescimento fetal",
                "Verificar posição do bebê",
                "Finalizar preparação do quarto",
                "Lavar e organizar roupinhas do bebê",
                "Montar o berço e checar segurança",
                "Preparar kit de higiene do bebê",
                "Definir pediatra do bebê",
                "Revisar plano de parto"
            ),
            tips = listOf(
                "O bebê está ganhando peso rapidamente",
                "Podem aparecer contrações de treinamento (Braxton Hicks)"
            )
        ),
        WeeklyChecklist(
            week = 36,
            title = "Semana 36",
            description = "Preparação final",
            emoji = "🍼",
            items = listOf(
                "Consultas semanais a partir de agora",
                "Fazer exame de estreptococo do grupo B",
                "Terminar de organizar mala da maternidade",
                "Deixar mala do bebê pronta",
                "Confirmar documentos necessários",
                "Instalar bebê conforto no carro",
                "Deixar bateria do celular sempre carregada",
                "Definir quem vai te acompanhar no parto"
            ),
            tips = listOf(
                "O bebê pode encaixar na pelve a qualquer momento",
                "Descanse bastante e aproveite os últimos dias"
            )
        ),
        WeeklyChecklist(
            week = 38,
            title = "Semana 38",
            description = "Pode nascer a qualquer momento!",
            emoji = "⭐",
            items = listOf(
                "Manter consultas em dia",
                "Monitorar sinais de trabalho de parto",
                "Deixar tudo pronto para ir ao hospital",
                "Verificar caminho até a maternidade",
                "Ter telefones de emergência anotados",
                "Descansar o máximo possível",
                "Manter alimentação leve",
                "Praticar técnicas de respiração"
            ),
            tips = listOf(
                "Bebê já está praticamente formado",
                "Fique atenta aos sinais: contrações regulares, perda do tampão, ruptura da bolsa"
            )
        ),
        WeeklyChecklist(
            week = 40,
            title = "Semana 40",
            description = "A hora está chegando! 💕",
            emoji = "👶",
            items = listOf(
                "Manter calma e confiança",
                "Monitorar movimentos do bebê diariamente",
                "Seguir orientações médicas sobre indução (se necessário)",
                "Ter mala sempre à mão",
                "Manter hidratação",
                "Fazer caminhadas leves (se liberado)",
                "Aproveitar os últimos momentos da gravidez",
                "Confiar no seu corpo e na equipe médica"
            ),
            tips = listOf(
                "Muitos bebês nascem entre 40 e 41 semanas",
                "Confie no seu corpo - ele sabe o que fazer!"
            )
        )
    )
    
    /**
     * Retorna o checklist mais próximo da semana atual
     */
    fun getChecklistForWeek(currentWeek: Int): WeeklyChecklist? {
        return weeklyChecklists.lastOrNull { it.week <= currentWeek }
    }
    
    /**
     * Retorna todos os checklists até a semana atual
     */
    fun getChecklistsUpToWeek(currentWeek: Int): List<WeeklyChecklist> {
        return weeklyChecklists.filter { it.week <= currentWeek }
    }
}

