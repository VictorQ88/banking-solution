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

- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- PostgreSQL: puerto 5432