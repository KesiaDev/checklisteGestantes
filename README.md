# Checklist para Gestantes 🤰

Aplicativo Android simples para ajudar gestantes a se organizarem durante a gestação com checklists offline.

## ✨ Funcionalidades

- **Mala da Maternidade**: Lista de itens essenciais para levar ao hospital
- **Pré-natal**: Acompanhamento de consultas, exames e vacinas
- **Pós-parto**: Organização para os primeiros dias com o bebê

## 🛠️ Tecnologias

- Kotlin
- Jetpack Compose
- Material Design 3
- Room Database (persistência local)
- MVVM Architecture
- Navigation Compose

## 📱 Características

- ✅ 100% Offline - Funciona sem internet
- ✅ Sem login necessário
- ✅ Dados salvos localmente
- ✅ Interface acolhedora e feminina
- ✅ Cores suaves (tons pastel)
- ✅ Barra de progresso visual

## 🚀 Como Compilar

### Pré-requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17
- Android SDK 34

### Build Debug
```bash
./gradlew assembleDebug
```

### Build Release (APK)
```bash
./gradlew assembleRelease
```

### Build Release (AAB para Play Store)
```bash
./gradlew bundleRelease
```

## 📦 Gerar APK Assinado

1. Abra o projeto no Android Studio
2. Vá em **Build > Generate Signed Bundle/APK**
3. Escolha APK ou Android App Bundle
4. Configure ou crie sua keystore
5. Selecione release
6. Clique em Create

## 📂 Estrutura do Projeto

```
app/src/main/java/com/gestantes/checklist/
├── data/
│   ├── dao/           # Data Access Objects (Room)
│   ├── database/      # Configuração do banco
│   └── entity/        # Entidades do banco
├── navigation/        # Navegação Compose
├── ui/
│   ├── checklist/     # Tela de checklist
│   ├── home/          # Tela inicial
│   └── theme/         # Cores, tipografia e tema
├── viewmodel/         # ViewModels
├── ChecklistApp.kt    # Application class
└── MainActivity.kt    # Activity principal
```

## ⚖️ Aviso Legal

Este aplicativo não substitui acompanhamento médico. Seu objetivo é apenas auxiliar na organização da rotina da gestante.

## 📄 Licença

Uso pessoal e comercial permitido.

