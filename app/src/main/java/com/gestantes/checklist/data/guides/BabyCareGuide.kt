package com.gestantes.checklist.data.guides

/**
 * Guia de cuidados e dúvidas frequentes para mamães de primeira viagem.
 * 
 * ⚠️ IMPORTANTE: Este guia oferece apenas orientações gerais.
 * NÃO substitui avaliação e acompanhamento médico.
 * Em caso de dúvida ou emergência, procure atendimento médico.
 */
object BabyCareGuide {

    data class CareCategory(
        val id: String,
        val name: String,
        val icon: String,
        val description: String,
        val topics: List<CareTopic>
    )

    data class CareTopic(
        val id: String,
        val title: String,
        val content: String,
        val tips: List<String>,
        val whenToSeeDoctor: List<String>,
        val keywords: List<String> // Para busca
    )

    val categories = listOf(
        // FEBRE
        CareCategory(
            id = "fever",
            name = "Febre",
            icon = "🌡️",
            description = "Como identificar e o que fazer quando o bebê está com febre",
            topics = listOf(
                CareTopic(
                    id = "fever_basics",
                    title = "O que é febre e como medir",
                    content = """
                        A febre é uma resposta do corpo a infecções ou inflamações. Em bebês, é considerada febre quando a temperatura está:
                        
                        • Acima de 37,8°C (axilar)
                        • Acima de 38°C (retal)
                        
                        Como medir corretamente:
                        • Termômetro digital é o mais seguro
                        • Axilar: mantenha por 3-5 minutos
                        • Espere 30 min após banho ou mamada
                        • Anote horário e temperatura
                    """.trimIndent(),
                    tips = listOf(
                        "Mantenha um termômetro digital em casa",
                        "Use roupas leves quando estiver com febre",
                        "Ofereça líquidos com frequência",
                        "Banho morno (não frio!) pode ajudar",
                        "Não use álcool ou gelo para baixar febre",
                        "Paracetamol ou dipirona apenas se prescrito pelo pediatra"
                    ),
                    whenToSeeDoctor = listOf(
                        "Bebê menor de 3 meses COM QUALQUER febre",
                        "Febre acima de 39°C",
                        "Febre que não baixa com antitérmico",
                        "Bebê muito irritado ou apático",
                        "Febre por mais de 3 dias",
                        "Manchas no corpo junto com febre",
                        "Convulsão febril"
                    ),
                    keywords = listOf("febre", "temperatura", "termômetro", "quente", "febrícola")
                ),
                CareTopic(
                    id = "fever_convulsion",
                    title = "Convulsão Febril",
                    content = """
                        Convulsões febris acontecem em algumas crianças quando a temperatura sobe rapidamente. Afetam 2-5% das crianças entre 6 meses e 5 anos.
                        
                        Durante a convulsão:
                        • MANTENHA A CALMA
                        • Coloque a criança de lado
                        • NÃO coloque nada na boca
                        • NÃO tente segurar
                        • Afaste objetos perigosos
                        • Marque o tempo de duração
                        
                        A maioria dura menos de 5 minutos e não causa danos.
                    """.trimIndent(),
                    tips = listOf(
                        "Após a convulsão, a criança pode ficar sonolenta - é normal",
                        "Trate a febre assim que perceber",
                        "Histórico familiar aumenta o risco",
                        "Não significa epilepsia"
                    ),
                    whenToSeeDoctor = listOf(
                        "SEMPRE após primeira convulsão",
                        "Convulsão que dura mais de 5 minutos",
                        "Criança não acorda após convulsão",
                        "Mais de uma convulsão em 24 horas",
                        "Criança fica com alguma parte do corpo fraca"
                    ),
                    keywords = listOf("convulsão", "febril", "tremores", "desmaio", "ataque")
                )
            )
        ),

        // PELE
        CareCategory(
            id = "skin",
            name = "Pele do Bebê",
            icon = "👶",
            description = "Manchas, irritações e cuidados com a pele delicada",
            topics = listOf(
                CareTopic(
                    id = "diaper_rash",
                    title = "Assadura / Dermatite de Fralda",
                    content = """
                        A assadura é muito comum! A pele fica vermelha, irritada e às vezes com bolinhas na área da fralda.
                        
                        Causas comuns:
                        • Fralda muito tempo úmida/suja
                        • Reação a produtos
                        • Introdução de novos alimentos
                        • Antibióticos
                        • Diarreia
                    """.trimIndent(),
                    tips = listOf(
                        "Troque a fralda com frequência",
                        "Deixe o bumbum arejando sempre que possível",
                        "Use água morna e algodão (evite lenços com álcool)",
                        "Seque bem antes de colocar a fralda",
                        "Use pomada de barreira (óxido de zinco)",
                        "Evite talcos",
                        "Deixe a fralda mais solta"
                    ),
                    whenToSeeDoctor = listOf(
                        "Assadura que não melhora em 3 dias",
                        "Feridas abertas ou sangrando",
                        "Pus ou secreção",
                        "Febre junto com assadura",
                        "Bolinhas que se espalham além da área da fralda"
                    ),
                    keywords = listOf("assadura", "dermatite", "fralda", "bumbum", "vermelho", "irritação")
                ),
                CareTopic(
                    id = "baby_acne",
                    title = "Acne Neonatal",
                    content = """
                        Espinhas no rosto do bebê são NORMAIS! Aparecem geralmente entre 2-4 semanas de vida.
                        
                        Características:
                        • Pequenas bolinhas vermelhas ou brancas
                        • Principalmente no rosto
                        • Causadas pelos hormônios da mãe
                        • Desaparecem sozinhas em algumas semanas
                    """.trimIndent(),
                    tips = listOf(
                        "NÃO esprema as espinhas",
                        "Lave o rosto com água morna",
                        "Não use cremes ou produtos",
                        "Evite passar leite materno",
                        "Paciência - vai passar!"
                    ),
                    whenToSeeDoctor = listOf(
                        "Espinhas que persistem além de 3 meses",
                        "Pus ou infecção",
                        "Espinhas em outras partes do corpo"
                    ),
                    keywords = listOf("acne", "espinhas", "bolinhas", "rosto", "bebê")
                ),
                CareTopic(
                    id = "milia",
                    title = "Milium (Bolinhas Brancas)",
                    content = """
                        Pequenas bolinhas brancas no nariz, bochechas ou queixo do recém-nascido.
                        
                        Características:
                        • Parecem pequenas pérolas
                        • São cistos minúsculos
                        • Completamente normais e inofensivos
                        • Desaparecem sozinhos em semanas
                    """.trimIndent(),
                    tips = listOf(
                        "Não tente remover ou espremer",
                        "Não aplique cremes",
                        "Apenas mantenha a pele limpa",
                        "Desaparecem naturalmente"
                    ),
                    whenToSeeDoctor = listOf(
                        "Raramente necessário",
                        "Se houver inflamação ou pus"
                    ),
                    keywords = listOf("milium", "bolinhas brancas", "pérolas", "nariz", "cistos")
                ),
                CareTopic(
                    id = "cradle_cap",
                    title = "Crosta Láctea",
                    content = """
                        Escamas amareladas ou esbranquiçadas no couro cabeludo do bebê. Muito comum!
                        
                        Características:
                        • Escamas gordurosas no couro cabeludo
                        • Pode aparecer nas sobrancelhas também
                        • NÃO é falta de higiene
                        • NÃO é contagioso
                        • Causada por glândulas sebáceas
                    """.trimIndent(),
                    tips = listOf(
                        "Passe óleo vegetal ou de amêndoas antes do banho",
                        "Deixe agir 15-20 minutos",
                        "Massageie suavemente com escova macia",
                        "Lave com shampoo neutro",
                        "Repita algumas vezes por semana",
                        "Não arranque as crostas secas"
                    ),
                    whenToSeeDoctor = listOf(
                        "Se a área ficar vermelha ou inflamada",
                        "Se espalhar para rosto ou corpo",
                        "Se houver coceira intensa",
                        "Se não melhorar com cuidados caseiros"
                    ),
                    keywords = listOf("crosta láctea", "caspa", "escamas", "couro cabeludo", "seborreia")
                ),
                CareTopic(
                    id = "eczema",
                    title = "Eczema / Dermatite Atópica",
                    content = """
                        Pele seca, vermelha e com coceira. Pode aparecer em qualquer idade.
                        
                        Locais comuns:
                        • Bebês: bochechas, couro cabeludo, dobras
                        • Crianças maiores: dobras dos cotovelos e joelhos
                        
                        Não é contagioso e tem componente genético/alérgico.
                    """.trimIndent(),
                    tips = listOf(
                        "Hidrate a pele várias vezes ao dia",
                        "Use sabonete neutro e suave",
                        "Banhos curtos e mornos (não quentes)",
                        "Roupas de algodão",
                        "Evite amaciante de roupas",
                        "Mantenha unhas do bebê curtas",
                        "Identifique e evite gatilhos"
                    ),
                    whenToSeeDoctor = listOf(
                        "Sempre que suspeitar de eczema",
                        "Se a pele infeccionar (pus, febre)",
                        "Se coceira intensa atrapalhar o sono",
                        "Se não melhorar com hidratação"
                    ),
                    keywords = listOf("eczema", "atópica", "seca", "coceira", "alergia", "vermelha")
                ),
                CareTopic(
                    id = "heat_rash",
                    title = "Brotoeja / Miliária",
                    content = """
                        Bolinhas pequenas causadas pelo suor retido. Comum em dias quentes ou quando o bebê está muito agasalhado.
                        
                        Tipos:
                        • Cristalina: bolinhas transparentes
                        • Rubra: bolinhas vermelhas com coceira
                    """.trimIndent(),
                    tips = listOf(
                        "Mantenha o bebê em ambiente fresco",
                        "Vista roupas leves de algodão",
                        "Evite agasalhar demais",
                        "Banhos frescos",
                        "Deixe a pele respirar",
                        "Evite cremes gordurosos"
                    ),
                    whenToSeeDoctor = listOf(
                        "Se houver pus",
                        "Se o bebê tiver febre",
                        "Se não melhorar em alguns dias",
                        "Se as lesões aumentarem"
                    ),
                    keywords = listOf("brotoeja", "miliária", "calor", "suor", "bolinhas", "verão")
                )
            )
        ),

        // ALIMENTAÇÃO
        CareCategory(
            id = "feeding",
            name = "Alimentação",
            icon = "🍼",
            description = "Amamentação, fórmula e introdução alimentar",
            topics = listOf(
                CareTopic(
                    id = "breastfeeding_tips",
                    title = "Amamentação - Dicas Gerais",
                    content = """
                        O leite materno é o alimento ideal para o bebê até os 6 meses (exclusivo) e complementar até 2 anos ou mais.
                        
                        Frequência:
                        • Livre demanda (quando o bebê pedir)
                        • Recém-nascidos: 8-12 vezes por dia
                        • Confie no seu corpo e no bebê
                    """.trimIndent(),
                    tips = listOf(
                        "Posição confortável é fundamental",
                        "Bebê deve abocanhar a aréola, não só o mamilo",
                        "Ofereça os dois seios a cada mamada",
                        "Beba bastante água",
                        "Descanse sempre que possível",
                        "Esvazie bem as mamas",
                        "Não existe leite fraco!"
                    ),
                    whenToSeeDoctor = listOf(
                        "Dor intensa ao amamentar",
                        "Fissuras que não cicatrizam",
                        "Mama muito vermelha, quente ou empedrada",
                        "Febre junto com dor na mama",
                        "Bebê não está ganhando peso"
                    ),
                    keywords = listOf("amamentação", "peito", "leite materno", "mamar", "mamada")
                ),
                CareTopic(
                    id = "colic",
                    title = "Cólicas",
                    content = """
                        Choro intenso, geralmente no fim da tarde/início da noite, em bebês saudáveis.
                        
                        Regra dos 3:
                        • Mais de 3 horas de choro por dia
                        • Mais de 3 dias por semana
                        • Por mais de 3 semanas
                        
                        Geralmente começa por volta de 2-3 semanas e melhora aos 3-4 meses.
                    """.trimIndent(),
                    tips = listOf(
                        "Movimento ajuda: embale, balance, ande",
                        "Som: ruído branco, 'shhhh', secador",
                        "Sucção: peito, chupeta, dedo limpo",
                        "Enrolar (swaddle) pode acalmar",
                        "Massagem na barriguinha em sentido horário",
                        "Posição de avião (bruços no antebraço)",
                        "Reveze os cuidados - você também precisa de pausa"
                    ),
                    whenToSeeDoctor = listOf(
                        "Choro o dia todo (não só fim da tarde)",
                        "Vômitos em jato",
                        "Febre",
                        "Sangue nas fezes",
                        "Não está ganhando peso",
                        "Você está exausta - peça ajuda!"
                    ),
                    keywords = listOf("cólica", "choro", "gases", "barriga", "dor")
                ),
                CareTopic(
                    id = "reflux",
                    title = "Refluxo / Golfadas",
                    content = """
                        É normal bebês golfarem um pouco de leite após mamar. Diferente de vômito!
                        
                        Refluxo fisiológico:
                        • Pequenas quantidades após mamadas
                        • Bebê está bem e ganhando peso
                        • Melhora com o tempo (geralmente até 1 ano)
                    """.trimIndent(),
                    tips = listOf(
                        "Mantenha o bebê em pé 20-30 min após mamar",
                        "Faça ele arrotar durante e após mamadas",
                        "Não deite logo após mamar",
                        "Evite roupas apertadas na barriga",
                        "Mamadas menores e mais frequentes",
                        "Elevar o colchão NÃO é recomendado para dormir"
                    ),
                    whenToSeeDoctor = listOf(
                        "Vômitos em jato e frequentes",
                        "Vômito verde ou com sangue",
                        "Não está ganhando peso",
                        "Chora muito durante ou após mamadas",
                        "Tosse ou engasgos frequentes",
                        "Arqueia as costas de dor"
                    ),
                    keywords = listOf("refluxo", "golfada", "vômito", "regurgitação", "leite volta")
                ),
                CareTopic(
                    id = "food_introduction",
                    title = "Introdução Alimentar (6 meses)",
                    content = """
                        A partir dos 6 meses, o bebê pode começar a comer alimentos além do leite.
                        
                        Sinais de prontidão:
                        • Sustenta a cabeça e senta com apoio
                        • Demonstra interesse pela comida
                        • Leva objetos à boca
                        • Perdeu reflexo de protrusão da língua
                    """.trimIndent(),
                    tips = listOf(
                        "Comece devagar - uma refeição por dia",
                        "Frutas, legumes e verduras primeiro",
                        "Ofereça um alimento novo por vez (3 dias)",
                        "Textura: amassada, nunca liquidificada",
                        "BLW (pedaços) ou tradicional - ambos funcionam",
                        "Leite continua sendo principal até 1 ano",
                        "Não adicione sal ou açúcar",
                        "Coma junto com o bebê - exemplo!"
                    ),
                    whenToSeeDoctor = listOf(
                        "Alergia alimentar (manchas, inchaço, vômitos)",
                        "Recusa persistente de alimentos",
                        "Não ganha peso adequadamente",
                        "Engasgos frequentes"
                    ),
                    keywords = listOf("introdução alimentar", "comida", "papinha", "BLW", "6 meses", "sólidos")
                )
            )
        ),

        // SONO
        CareCategory(
            id = "sleep_issues",
            name = "Problemas de Sono",
            icon = "😴",
            description = "Dificuldades comuns com o sono do bebê",
            topics = listOf(
                CareTopic(
                    id = "day_night_confusion",
                    title = "Troca do Dia pela Noite",
                    content = """
                        Muito comum nas primeiras semanas! O bebê ainda não produz melatonina e não distingue dia de noite.
                        
                        Geralmente melhora por volta de 6-8 semanas quando o ritmo circadiano começa a se desenvolver.
                    """.trimIndent(),
                    tips = listOf(
                        "Durante o dia: ambiente claro, barulho normal, interação",
                        "À noite: ambiente escuro, silencioso, pouca interação",
                        "Exponha o bebê à luz natural durante o dia",
                        "Não feche cortinas para sonecas diurnas (início)",
                        "Mamadas noturnas: luz baixa, sem conversa",
                        "Paciência - vai melhorar!"
                    ),
                    whenToSeeDoctor = listOf(
                        "Se persistir além de 8-10 semanas",
                        "Se bebê parecer muito irritado/desconfortável"
                    ),
                    keywords = listOf("dia", "noite", "troca", "acordado", "madrugada")
                ),
                CareTopic(
                    id = "sleep_regression",
                    title = "Regressões de Sono",
                    content = """
                        São fases em que o bebê que dormia bem passa a acordar mais. É temporário!
                        
                        Principais regressões:
                        • 4 meses (mudança nos ciclos de sono)
                        • 8-10 meses (marcos motores, ansiedade separação)
                        • 12 meses (andar, independência)
                        • 18 meses (uma das mais intensas)
                        • 2 anos (pesadelos, medos)
                    """.trimIndent(),
                    tips = listOf(
                        "Mantenha a rotina mesmo que pareça não funcionar",
                        "Não crie novos hábitos (ex: voltar a ninar)",
                        "Ofereça conforto sem exagerar",
                        "Seja consistente",
                        "Isso vai passar! Geralmente 2-6 semanas"
                    ),
                    whenToSeeDoctor = listOf(
                        "Se a 'regressão' durar mais de 6 semanas",
                        "Se bebê parecer doente ou com dor",
                        "Se você estiver exausta - peça ajuda!"
                    ),
                    keywords = listOf("regressão", "sono", "acordar", "piorou", "voltou")
                ),
                CareTopic(
                    id = "nightmares",
                    title = "Pesadelos e Terrores Noturnos",
                    content = """
                        Pesadelos: A criança acorda assustada e se lembra do sonho ruim. Mais comum após 2 anos.
                        
                        Terror noturno: A criança grita/chora mas está dormindo. Não se lembra depois. Acontece nas primeiras horas de sono.
                    """.trimIndent(),
                    tips = listOf(
                        "Pesadelo: acolha, converse, acalme",
                        "Terror noturno: NÃO acorde a criança",
                        "Terror: apenas garanta segurança e espere passar",
                        "Evite telas antes de dormir",
                        "Mantenha rotina calma à noite",
                        "Evite histórias assustadoras"
                    ),
                    whenToSeeDoctor = listOf(
                        "Terror noturno muito frequente",
                        "Criança se machuca durante terror",
                        "Pesadelos persistentes que prejudicam o dia"
                    ),
                    keywords = listOf("pesadelo", "terror noturno", "medo", "grito", "sonho")
                )
            )
        ),

        // EMERGÊNCIAS
        CareCategory(
            id = "emergency",
            name = "Emergências",
            icon = "🚨",
            description = "Quando procurar atendimento imediato",
            topics = listOf(
                CareTopic(
                    id = "emergency_signs",
                    title = "Sinais de Emergência",
                    content = """
                        ⚠️ PROCURE ATENDIMENTO IMEDIATO se o bebê apresentar:
                        
                        • Dificuldade para respirar
                        • Lábios ou unhas azulados
                        • Convulsões
                        • Febre em bebê menor de 3 meses
                        • Vômito verde ou com sangue
                        • Sangue nas fezes
                        • Não acorda/não reage
                        • Fontanela (moleira) muito funda ou abaulada
                        • Manchas roxas que não somem ao pressionar
                    """.trimIndent(),
                    tips = listOf(
                        "Tenha o telefone do pediatra sempre à mão",
                        "Saiba onde fica a emergência pediátrica mais próxima",
                        "Em dúvida, procure atendimento - sempre!",
                        "Confie no seu instinto de mãe"
                    ),
                    whenToSeeDoctor = listOf(
                        "QUALQUER um dos sinais acima = emergência",
                        "Se você sentir que algo está errado"
                    ),
                    keywords = listOf("emergência", "urgente", "grave", "perigo", "hospital")
                ),
                CareTopic(
                    id = "choking",
                    title = "Engasgo",
                    content = """
                        O que fazer se o bebê engasgar:
                        
                        SE ESTÁ TOSSINDO FORTE:
                        • Incentive a tossir
                        • NÃO dê tapas nem enfie o dedo
                        
                        SE NÃO ESTÁ TOSSINDO/RESPIRANDO:
                        • Bebê de bruços no seu antebraço
                        • Cabeça mais baixa que o corpo
                        • 5 tapas firmes nas costas
                        • Vire e dê 5 compressões no peito
                        • Repita até sair ou chegar ajuda
                    """.trimIndent(),
                    tips = listOf(
                        "Faça curso de primeiros socorros",
                        "Nunca deixe objetos pequenos ao alcance",
                        "Alimentos devem ter tamanho seguro",
                        "Supervisione sempre durante refeições",
                        "Bebê sentado para comer"
                    ),
                    whenToSeeDoctor = listOf(
                        "LIGUE 192 (SAMU) se bebê não respirar",
                        "Após engasgo severo, mesmo que resolvido"
                    ),
                    keywords = listOf("engasgo", "engasgou", "sufocando", "não respira", "asfixia")
                )
            )
        ),

        // COMPORTAMENTO
        CareCategory(
            id = "behavior",
            name = "Comportamento",
            icon = "👶",
            description = "Entendendo o comportamento do seu bebê",
            topics = listOf(
                CareTopic(
                    id = "crying",
                    title = "Por que o Bebê Chora?",
                    content = """
                        O choro é a única forma do bebê se comunicar. Principais motivos:
                        
                        • Fome
                        • Fralda suja/molhada
                        • Sono
                        • Quer colo/aconchego
                        • Calor ou frio
                        • Desconforto (roupa, posição)
                        • Superestimulação
                        • Cólicas
                        • Dentição
                    """.trimIndent(),
                    tips = listOf(
                        "Checklist: fome → fralda → sono → colo",
                        "Às vezes só precisa de aconchego",
                        "Pele a pele acalma",
                        "Movimento ritmado ajuda",
                        "Som: 'shhhh', ruído branco",
                        "Você NÃO está mimando ao atender o choro"
                    ),
                    whenToSeeDoctor = listOf(
                        "Choro muito diferente do habitual",
                        "Choro agudo e inconsolável",
                        "Choro fraco/gemido contínuo",
                        "Febre junto com choro"
                    ),
                    keywords = listOf("choro", "chorar", "chorando", "não para", "consolar")
                ),
                CareTopic(
                    id = "tantrums",
                    title = "Birras (1-3 anos)",
                    content = """
                        Birras são NORMAIS e fazem parte do desenvolvimento! A criança está aprendendo a lidar com frustrações.
                        
                        Por que acontecem:
                        • Cérebro ainda em desenvolvimento
                        • Não consegue expressar sentimentos em palavras
                        • Quer autonomia mas tem limitações
                        • Cansaço, fome, superestimulação
                    """.trimIndent(),
                    tips = listOf(
                        "Mantenha a calma (difícil, mas importante!)",
                        "Valide o sentimento: 'Você está bravo porque...'",
                        "Não ceda ao que causou a birra",
                        "Ofereça conforto se a criança aceitar",
                        "Após acalmar, converse sobre o ocorrido",
                        "Previna: rotina, sono adequado, escolhas"
                    ),
                    whenToSeeDoctor = listOf(
                        "Birras muito frequentes/intensas",
                        "Criança se machuca durante birras",
                        "Birras continuam intensas após 4 anos",
                        "Você está esgotada"
                    ),
                    keywords = listOf("birra", "pirraça", "grita", "esperneia", "manha", "chão")
                ),
                CareTopic(
                    id = "separation_anxiety",
                    title = "Ansiedade de Separação",
                    content = """
                        Fase normal que geralmente aparece entre 6-8 meses e pode ir até 2-3 anos.
                        
                        A criança chora quando você sai porque:
                        • Entende que você existe mesmo longe
                        • Mas não entende que você vai voltar
                        • É sinal de apego saudável!
                    """.trimIndent(),
                    tips = listOf(
                        "Não saia escondido - sempre se despeça",
                        "Despedidas curtas e confiantes",
                        "Crie ritual de despedida",
                        "Brinque de esconde-esconde",
                        "Objeto de transição (naninha) ajuda",
                        "Valide os sentimentos",
                        "Vai passar!"
                    ),
                    whenToSeeDoctor = listOf(
                        "Ansiedade muito intensa após 3-4 anos",
                        "Interfere muito no dia a dia",
                        "Outros sinais de ansiedade"
                    ),
                    keywords = listOf("separação", "ansiedade", "gruda", "não deixa", "sair", "escola")
                )
            )
        ),

        // HIGIENE
        CareCategory(
            id = "hygiene",
            name = "Higiene",
            icon = "🛁",
            description = "Cuidados com a higiene do bebê",
            topics = listOf(
                CareTopic(
                    id = "bath",
                    title = "Banho do Bebê",
                    content = """
                        O banho pode ser dado desde o nascimento, mas não precisa ser diário no início.
                        
                        Frequência:
                        • Recém-nascido: 2-3 vezes por semana é suficiente
                        • Bebê maior: pode ser diário se gostar
                    """.trimIndent(),
                    tips = listOf(
                        "Temperatura da água: 36-37°C (teste com cotovelo)",
                        "Ambiente aquecido",
                        "Tenha tudo à mão antes de começar",
                        "NUNCA deixe sozinho na água",
                        "Sabonete neutro e suave",
                        "Seque bem as dobrinhas",
                        "Banho pode ser momento de relaxamento"
                    ),
                    whenToSeeDoctor = listOf(
                        "Se a pele ficar muito ressecada",
                        "Irritações persistentes"
                    ),
                    keywords = listOf("banho", "banheira", "lavar", "água", "sabonete")
                ),
                CareTopic(
                    id = "umbilical_cord",
                    title = "Cuidados com o Umbigo",
                    content = """
                        O coto umbilical cai naturalmente entre 7-21 dias. Antes disso:
                        
                        • Mantenha limpo e SECO
                        • Não use álcool (não é mais recomendado)
                        • Fralda dobrada abaixo do coto
                        • Não puxe mesmo se estiver solto
                    """.trimIndent(),
                    tips = listOf(
                        "Limpe com gaze ou algodão e água limpa",
                        "Seque bem após o banho",
                        "Deixe exposto ao ar quando possível",
                        "É normal ter um cheirinho",
                        "Pode ter pequeno sangramento quando cai"
                    ),
                    whenToSeeDoctor = listOf(
                        "Vermelhidão ao redor do umbigo",
                        "Pus ou secreção amarelada",
                        "Cheiro muito forte/fétido",
                        "Sangramento que não para",
                        "Umbigo não cai após 3 semanas"
                    ),
                    keywords = listOf("umbigo", "coto umbilical", "cordão", "caiu", "infecção")
                ),
                CareTopic(
                    id = "nail_care",
                    title = "Corte de Unhas",
                    content = """
                        As unhas do bebê crescem rápido e podem arranhar o rostinho!
                        
                        Quando cortar:
                        • Quando estiverem compridas ou afiadas
                        • Aproximadamente 1x por semana (mãos)
                        • A cada 2 semanas (pés)
                    """.trimIndent(),
                    tips = listOf(
                        "Use cortador ou tesoura de ponta arredondada para bebês",
                        "Corte enquanto o bebê dorme (mais fácil!)",
                        "Corte reto, não arredondado",
                        "Se preferir, use lixa de bebê",
                        "Duas pessoas facilitam (uma segura, outra corta)"
                    ),
                    whenToSeeDoctor = listOf(
                        "Unha encravada com sinais de infecção",
                        "Vermelhidão ou pus ao redor da unha"
                    ),
                    keywords = listOf("unha", "cortar", "arranhar", "cortador", "lixa")
                )
            )
        )
    )

    // Função para buscar tópicos por palavra-chave
    fun searchTopics(query: String): List<Pair<CareCategory, CareTopic>> {
        val normalizedQuery = query.lowercase().trim()
        val results = mutableListOf<Pair<CareCategory, CareTopic>>()
        
        categories.forEach { category ->
            category.topics.forEach { topic ->
                val matchesKeyword = topic.keywords.any { it.contains(normalizedQuery) || normalizedQuery.contains(it) }
                val matchesTitle = topic.title.lowercase().contains(normalizedQuery)
                val matchesContent = topic.content.lowercase().contains(normalizedQuery)
                
                if (matchesKeyword || matchesTitle || matchesContent) {
                    results.add(category to topic)
                }
            }
        }
        
        return results
    }

    // Todas as palavras-chave para sugestões de busca
    val allKeywords: List<String>
        get() = categories.flatMap { it.topics.flatMap { topic -> topic.keywords } }.distinct().sorted()
}

