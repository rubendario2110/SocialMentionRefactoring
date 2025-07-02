# Social Mention Analyzer con Micronaut

Esta aplicación analiza menciones sociales y determina su nivel de riesgo usando Micronaut como framework HTTP.

## Requisitos

- Java 11 o superior
- Gradle

## Ejecutar la aplicación

### Desarrollo
```bash
./gradlew run
```

### Producción
```bash
./gradlew build
java -jar build/libs/social-mention-analyzer-1.0-SNAPSHOT.jar
```

## Endpoints disponibles

### Analizar mención de Facebook (POST)
```bash
curl -X POST http://localhost:8080/AnalyzeSocialMention \
  -H "Content-Type: application/json" \
  -d '{"message": "Mensaje de Facebook", "facebookAccount": "fb.com/xyz", "facebookComments": ["comentario 1", "comentario 2"]}'
```

### Analizar mención de Twitter (POST)
```bash
curl -X POST http://localhost:8080/AnalyzeSocialMention \
  -H "Content-Type: application/json" \
  -d '{"message": "Mensaje de Twitter", "tweeterAccount": "@usuario", "tweeterUrl": "https://twitter.com/usuario/status/123"}'
```

## Respuesta de ejemplo

```
HIGH_RISK
MEDIUM_RISK
LOW_RISK
Error, Tweeter or Facebook account must be present
```

## Configuración

La aplicación se ejecuta por defecto en el puerto 8080. Puedes modificar la configuración en `src/main/resources/application.yml`.

## Estructura del proyecto

- `api.controller`: Controladores HTTP
- `application.service`: Lógica de negocio
- `domain.model`: Modelos de dominio
- `domain.analyzer`: Analizadores específicos por plataforma
- `infrastructure.persistence`: Capa de persistencia 