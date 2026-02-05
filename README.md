# Banking Solution

Aplicación Full Stack (Spring Boot + PostgreSQL + Angular) para gestionar **Clientes**, **Cuentas**, **Movimientos** y generar **Reporte (Estado de cuenta)**.

## Requisitos
- Docker + Docker Compose
- (Opcional) Postman

---

## Levantar el proyecto con Docker

En la raíz del repositorio (donde está `docker-compose.yml`):

```bash
docker compose up --build


Servicios expuestos:

- Frontend: [http://localhost:4200](http://localhost:4200)
- Backend API: [http://localhost:8080/api](http://localhost:8080/api)
- PostgreSQL: puerto 5432

## Base de Datos

La base de datos se inicializa automáticamente con:

- Tablas:
  - clients
  - accounts
  - movements



Script utilizado:
- `BaseDatos.sql`


## Endpoints Principales

### Clientes
- `GET /api/clients`
- `POST /api/clients`
- `PUT /api/clients/{id}`
- `DELETE /api/clients/{id}`

### Cuentas
- `GET /api/accounts`
- `POST /api/accounts`
- `PUT /api/accounts/{id}`
- `DELETE /api/accounts/{id}`

### Movimientos
- `GET /api/movements`
- `GET /api/movements/account/{accountId}`
- `POST /api/movements`
- `DELETE /api/movements/{id}`

### Reporte
- `GET /api/reports?clientId={id}&from=YYYY-MM-DD&to=YYYY-MM-DD`

## Colección Postman

https://www.postman.com/sidra1-3519/banktests/collection/14993185-97aba2c4-47e5-4b5e-8218-f7d0b35f5d68



## Notas

- Hibernate configurado con `ddl-auto=validate`
- La base de datos debe existir antes de levantar el backend
- Eliminación en cascada de movimientos por cuenta
- Reporte generado en JSON y visualizado en frontend

---

## Autor

Victor Quimbiamba