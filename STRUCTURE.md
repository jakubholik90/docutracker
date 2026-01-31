document-flow-tracker/
│
├── src/
│   ├── main/
│   │   ├── java/com/yourname/documentflow/
│   │   │   │
│   │   │   ├── domain/              # CORE - Logika biznesowa (niezależna!)
│   │   │   │   ├── model/
│   │   │   │   │   ├── Document.java
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── DocumentStatus.java (enum: SENT_TO_SUBCONTRACTOR, WITH_INSPECTOR, WITH_CLIENT, APPROVED, REJECTED)
│   │   │   │   │   ├── Project.java
│   │   │   │   │   └── Deadline.java
│   │   │   │   │
│   │   │   │   ├── port/            # Interfejsy (porty)
│   │   │   │   │   ├── in/          # Porty wejściowe (use cases)
│   │   │   │   │   │   ├── CreateDocumentUseCase.java
│   │   │   │   │   │   ├── UpdateDocumentStatusUseCase.java
│   │   │   │   │   │   ├── GetProjectProgressUseCase.java
│   │   │   │   │   │   ├── CheckDeadlinesUseCase.java
│   │   │   │   │   │   └── SyncEmailsUseCase.java
│   │   │   │   │   │
│   │   │   │   │   └── out/         # Porty wyjściowe (do zewnętrznych systemów)
│   │   │   │   │       ├── DocumentRepository.java
│   │   │   │   │       ├── UserRepository.java
│   │   │   │   │       ├── EmailProvider.java
│   │   │   │   │       └── FileStorage.java
│   │   │   │   │
│   │   │   │   └── service/         # Implementacja logiki biznesowej
│   │   │   │       ├── DocumentService.java
│   │   │   │       ├── ProjectProgressService.java
│   │   │   │       └── DeadlineCheckService.java
│   │   │   │
│   │   │   ├── application/         # Warstwa aplikacji (orkiestracja)
│   │   │   │   ├── config/
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── OAuth2Config.java
│   │   │   │   │   └── SchedulingConfig.java
│   │   │   │   │
│   │   │   │   └── scheduler/
│   │   │   │       └── EmailSyncScheduler.java  # Co X minut pobiera emaile
│   │   │   │
│   │   │   ├── adapter/             # ADAPTERY - implementacje portów
│   │   │   │   ├── in/              # Adaptery wejściowe
│   │   │   │   │   ├── web/         # REST Controllers
│   │   │   │   │   │   ├── DocumentController.java
│   │   │   │   │   │   ├── ProjectController.java
│   │   │   │   │   │   ├── UserController.java
│   │   │   │   │   │   └── dto/     # DTOs do REST API
│   │   │   │   │   │       ├── DocumentRequest.java
│   │   │   │   │   │       ├── DocumentResponse.java
│   │   │   │   │   │       └── ProjectProgressResponse.java
│   │   │   │   │   │
│   │   │   │   │   └── ui/          # Thymeleaf Controllers
│   │   │   │   │       ├── DashboardViewController.java
│   │   │   │   │       ├── DocumentViewController.java
│   │   │   │   │       └── LoginViewController.java
│   │   │   │   │
│   │   │   │   └── out/             # Adaptery wyjściowe
│   │   │   │       ├── persistence/ # Implementacja Repository
│   │   │   │       │   ├── jpa/
│   │   │   │       │   │   ├── DocumentJpaRepository.java (Spring Data)
│   │   │   │       │   │   ├── UserJpaRepository.java
│   │   │   │       │   │   └── entity/  # JPA Entities (RÓŻNE od domain models!)
│   │   │   │       │   │       ├── DocumentEntity.java
│   │   │   │       │   │       └── UserEntity.java
│   │   │   │       │   │
│   │   │   │       │   └── DocumentRepositoryAdapter.java  # Mapuje Entity <-> Domain
│   │   │   │       │
│   │   │   │       ├── email/       # Gmail integration
│   │   │   │       │   ├── GmailEmailProvider.java
│   │   │   │       │   └── GmailOAuth2Handler.java
│   │   │   │       │
│   │   │   │       └── storage/     # Przechowywanie PDF
│   │   │   │           └── LocalFileStorageAdapter.java  # Na początek lokalnie
│   │   │   │
│   │   │   └── infrastructure/      # Cross-cutting concerns
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── DocumentNotFoundException.java
│   │   │       │   └── EmailSyncException.java
│   │   │       │
│   │   │       └── security/
│   │   │           └── CustomOAuth2UserService.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── db/migration/        # Flyway migrations
│   │       │   ├── V1__create_users_table.sql
│   │       │   ├── V2__create_documents_table.sql
│   │       │   └── V3__create_projects_table.sql
│   │       │
│   │       ├── static/              # CSS, JS
│   │       │   ├── css/
│   │       │   └── js/
│   │       │
│   │       └── templates/           # Thymeleaf
│   │           ├── login.html
│   │           ├── dashboard.html
│   │           ├── documents.html
│   │           └── project-progress.html
│   │
│   └── test/
│       └── java/com/yourname/documentflow/
│           ├── domain/              # Unit testy (NAJWAŻNIEJSZE w TDD!)
│           │   └── service/
│           │       ├── DocumentServiceTest.java
│           │       └── DeadlineCheckServiceTest.java
│           │
│           ├── adapter/
│           │   ├── web/             # Integration testy REST API
│           │   │   └── DocumentControllerTest.java
│           │   │
│           │   └── persistence/     # Testy repository z Testcontainers
│           │       └── DocumentRepositoryAdapterTest.java
│           │
│           └── integration/         # End-to-end testy
│               └── EmailSyncIntegrationTest.java
│
├── docker-compose.yml               # PostgreSQL + aplikacja
├── Dockerfile
├── pom.xml (lub build.gradle)
└── README.md