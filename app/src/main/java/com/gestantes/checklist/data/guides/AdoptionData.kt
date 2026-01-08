package com.gestantes.checklist.data.guides

/**
 * Dados e guias para famílias em processo de adoção
 * 
 * ADITIVO - Novo módulo para apoiar pais e mães adotivos
 * 
 * A adoção é uma "gestação do coração" - uma jornada igualmente
 * intensa, cheia de amor, expectativa e preparação.
 */
object AdoptionData {
    
    /**
     * Fases do processo de adoção no Brasil
     */
    val adoptionPhases = listOf(
        AdoptionPhase(
            id = 1,
            title = "Decisão e Preparação",
            emoji = "💭",
            description = "O momento de refletir, pesquisar e tomar a decisão de adotar",
            tips = listOf(
                "Converse com sua família sobre a decisão de adotar",
                "Pesquise sobre o processo de adoção no Brasil",
                "Reflita sobre suas motivações e expectativas",
                "Converse com famílias que já adotaram",
                "Prepare-se emocionalmente para a jornada"
            ),
            checklist = listOf(
                "Conversar em família sobre a decisão",
                "Pesquisar sobre o processo de adoção",
                "Ler sobre experiências de outras famílias",
                "Refletir sobre o perfil da criança desejada",
                "Buscar informações na Vara da Infância"
            )
        ),
        AdoptionPhase(
            id = 2,
            title = "Habilitação",
            emoji = "📋",
            description = "Cadastro e preparação junto à Vara da Infância e Juventude",
            tips = listOf(
                "Procure a Vara da Infância da sua cidade",
                "Participe do curso preparatório obrigatório",
                "Seja sincero nas entrevistas e avaliações",
                "Mantenha documentos sempre atualizados",
                "Tenha paciência - cada etapa tem seu tempo"
            ),
            checklist = listOf(
                "Ir à Vara da Infância e Juventude",
                "Preencher o formulário de cadastro",
                "Reunir documentos necessários",
                "Participar do curso preparatório",
                "Realizar entrevistas com assistente social",
                "Realizar avaliação psicológica",
                "Aguardar aprovação do cadastro"
            )
        ),
        AdoptionPhase(
            id = 3,
            title = "Espera Ativa",
            emoji = "⏰",
            description = "Período de aguardar a compatibilidade com uma criança",
            tips = listOf(
                "Use esse tempo para se preparar ainda mais",
                "Mantenha seu cadastro atualizado",
                "Participe de grupos de apoio à adoção",
                "Prepare o ambiente para a chegada",
                "Cuide da sua saúde emocional durante a espera"
            ),
            checklist = listOf(
                "Manter cadastro atualizado",
                "Participar de grupos de apoio",
                "Preparar o quarto/espaço",
                "Organizar itens necessários",
                "Informar-se sobre a criança que chegará",
                "Preparar a rede de apoio familiar"
            )
        ),
        AdoptionPhase(
            id = 4,
            title = "Aproximação",
            emoji = "🤝",
            description = "Período de conhecer e criar vínculo com a criança",
            tips = listOf(
                "Respeite o tempo da criança",
                "Seja paciente e amoroso",
                "Observe e ouça mais do que fale",
                "Construa o vínculo aos poucos",
                "Mantenha contato com a equipe técnica"
            ),
            checklist = listOf(
                "Primeira visita à criança",
                "Visitas regulares de aproximação",
                "Atividades conjuntas supervisionadas",
                "Primeiras saídas com a criança",
                "Pernoites graduais",
                "Acompanhamento com equipe técnica"
            )
        ),
        AdoptionPhase(
            id = 5,
            title = "Guarda Provisória",
            emoji = "🏠",
            description = "A criança vem morar com você durante o estágio de convivência",
            tips = listOf(
                "Estabeleça uma rotina tranquila",
                "Dê tempo para adaptação de todos",
                "Mantenha comunicação com a equipe",
                "Celebre cada pequeno progresso",
                "Busque apoio quando precisar"
            ),
            checklist = listOf(
                "Receber a criança em casa",
                "Estabelecer rotina diária",
                "Apresentar à família e amigos próximos",
                "Acompanhamento psicológico",
                "Relatórios de convivência",
                "Audiência de avaliação"
            )
        ),
        AdoptionPhase(
            id = 6,
            title = "Adoção Definitiva",
            emoji = "💕",
            description = "A família está oficialmente formada!",
            tips = listOf(
                "Celebre esse momento especial!",
                "Continue construindo o vínculo",
                "Mantenha acompanhamento se necessário",
                "Conte a história da adoção com amor",
                "Sua família está completa! 💕"
            ),
            checklist = listOf(
                "Audiência final de adoção",
                "Nova certidão de nascimento",
                "Atualizar documentos",
                "Celebrar em família!",
                "Registrar esse momento especial"
            )
        )
    )
    
    /**
     * Checklist geral de preparação para adoção
     */
    val preparationChecklist = listOf(
        AdoptionChecklistItem(
            category = "Documentação",
            items = listOf(
                "RG e CPF dos adotantes",
                "Certidão de casamento ou nascimento",
                "Comprovante de residência",
                "Comprovante de renda",
                "Atestado de sanidade física e mental",
                "Certidões negativas (civil e criminal)",
                "Fotos da família e da casa"
            )
        ),
        AdoptionChecklistItem(
            category = "Preparação do Lar",
            items = listOf(
                "Quarto ou espaço para a criança",
                "Móveis adequados à idade",
                "Roupas de cama e banho",
                "Itens de higiene",
                "Brinquedos e livros",
                "Cadeirinha de carro (se necessário)",
                "Adaptações de segurança"
            )
        ),
        AdoptionChecklistItem(
            category = "Preparação Emocional",
            items = listOf(
                "Curso preparatório concluído",
                "Participação em grupos de apoio",
                "Leitura sobre adoção",
                "Conversa com famílias adotivas",
                "Terapia/acompanhamento psicológico",
                "Preparação da família estendida",
                "Rede de apoio organizada"
            )
        ),
        AdoptionChecklistItem(
            category = "Após a Chegada",
            items = listOf(
                "Pediatra escolhido",
                "Escola/creche pesquisada",
                "Licença maternidade/paternidade",
                "Plano de saúde atualizado",
                "Rotina familiar planejada",
                "Tempo de adaptação reservado"
            )
        )
    )
    
    /**
     * Mensagens de apoio específicas para adoção
     */
    val supportMessages = listOf(
        // Validação
        "A adoção é um ato de amor imenso. Você está no caminho certo! 💕",
        "Família não é só sangue, é amor, escolha e compromisso. 💜",
        "Seu filho(a) está chegando. Cada passo te aproxima dele(a)! 🌟",
        "A espera é difícil, mas o encontro será transformador! ✨",
        "Você será o melhor pai/mãe para essa criança. Confie! 💪",
        
        // Durante a espera
        "Use esse tempo de espera para se preparar com amor. 📚",
        "Cada dia é um dia a menos para o grande encontro! 🎯",
        "A espera ativa é parte da gestação do coração. 💗",
        "Seu filho(a) também está esperando por você! 👶",
        "Mantenha a fé. O tempo certo vai chegar! 🙏",
        
        // Sobre o processo
        "O processo pode ser longo, mas vale cada momento! ⏰",
        "Cada etapa vencida é uma vitória. Celebre! 🎉",
        "Você não está sozinho(a) nessa jornada. 🤝",
        "Paciência e perseverança são suas aliadas! 💎",
        "Confie no processo e no seu amor! 💕",
        
        // Pós-adoção
        "A adaptação leva tempo. Seja paciente com todos! 🌸",
        "O vínculo se constrói dia a dia, com amor e presença. 🏠",
        "Seu filho(a) escolheu você tanto quanto você o escolheu! 💝",
        "A história de vocês começou no coração antes do encontro! ❤️",
        "Vocês são família. Isso é o que importa! 👨‍👩‍👧"
    )
    
    /**
     * Curiosidades sobre adoção
     */
    val funFacts = listOf(
        "No Brasil, cerca de 5 mil crianças são adotadas por ano! 📊",
        "A adoção tardia (crianças maiores) é um ato de amor imenso! 💕",
        "Grupos de irmãos podem ser adotados juntos, mantendo o vínculo! 👫",
        "O curso preparatório ajuda a família a se preparar emocionalmente! 📚",
        "Muitas famílias relatam que a espera valeu cada segundo! ⏰",
        "A licença maternidade/paternidade também vale para adoção! 📋",
        "O vínculo afetivo é tão forte quanto o biológico! 💗",
        "Crianças adotadas florescem com amor e segurança! 🌻",
        "A história da adoção deve ser contada com amor à criança! 📖",
        "Cada família adotiva transforma duas vidas: a sua e a da criança! ✨"
    )
    
    /**
     * Recursos úteis com links reais
     */
    val resources = listOf(
        AdoptionResource(
            title = "Cadastro Nacional de Adoção (CNA)",
            description = "Sistema oficial do CNJ para cadastro de pretendentes",
            type = ResourceType.WEBSITE,
            url = "https://www.cnj.jus.br/programas-e-acoes/adocao/"
        ),
        AdoptionResource(
            title = "Sistema Nacional de Adoção (SNA)",
            description = "Novo sistema do CNJ para adoção e acolhimento",
            type = ResourceType.WEBSITE,
            url = "https://www.cnj.jus.br/sna/"
        ),
        AdoptionResource(
            title = "Vara da Infância e Juventude",
            description = "Encontre a Vara da Infância da sua cidade",
            type = ResourceType.GOVERNMENT,
            url = "https://www.cnj.jus.br/corregedoria/justica_aberta/"
        ),
        AdoptionResource(
            title = "Grupos de Apoio à Adoção (GAAs)",
            description = "Grupos de famílias que compartilham experiências",
            type = ResourceType.SUPPORT_GROUP,
            url = "https://www.angaad.org.br/grupos-de-apoio"
        ),
        AdoptionResource(
            title = "ANGAAD",
            description = "Associação Nacional dos Grupos de Apoio à Adoção",
            type = ResourceType.NGO,
            url = "https://www.angaad.org.br/"
        ),
        AdoptionResource(
            title = "Adoção Brasil",
            description = "Portal com informações e orientações sobre adoção",
            type = ResourceType.WEBSITE,
            url = "https://www.adocaobrasil.com.br/"
        )
    )
    
    fun getPhaseById(id: Int): AdoptionPhase? {
        return adoptionPhases.find { it.id == id }
    }
    
    fun getRandomSupportMessage(): String {
        return supportMessages.random()
    }
    
    fun getRandomFunFact(): String {
        return funFacts.random()
    }
}

/**
 * Fase do processo de adoção
 */
data class AdoptionPhase(
    val id: Int,
    val title: String,
    val emoji: String,
    val description: String,
    val tips: List<String>,
    val checklist: List<String>
)

/**
 * Item de checklist de adoção
 */
data class AdoptionChecklistItem(
    val category: String,
    val items: List<String>
)

/**
 * Recurso útil para adoção com link
 */
data class AdoptionResource(
    val title: String,
    val description: String,
    val type: ResourceType,
    val url: String = "" // URL para abrir no navegador
)

enum class ResourceType {
    WEBSITE,
    GOVERNMENT,
    SUPPORT_GROUP,
    NGO,
    BOOK,
    APP
}
