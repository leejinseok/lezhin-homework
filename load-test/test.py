from locust import task, FastHttpUser, stats

stats.PERCENTILES_TO_CHART = [0.95, 0.99]


class LoadTest(FastHttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0

    @task
    def topThreeByLikes(self):
        headers = {
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsZXpoaW5AbGV6aGluLmNvbSIsImlkIjoxLCJyb2xlIjoiTUVNQkVSIiwiaWF0IjoxNzExMzIyNjY3LCJleHAiOjE3MTI2MTg2Njd9.Huv-fsDDIwgodEzK115X5Tarchy1v2IsbtclK7jYMjQ"
        }
        self.client.get("/api/v1/comics/top-three-by-likes", headers=headers)
