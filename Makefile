up:
	docker compose up -d --build

down:
	docker compose down

kill:
	docker compose down -v

status:
	docker ps -a
