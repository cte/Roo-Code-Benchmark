# Roo Code Benchmark Web UI

## Getting Started

Install dependencies:

```sh
pnpm install
```

Create your SQLite database:

```sh
cp .env.sample .env
# Update path to SQLite database as needed in `.env`.
npx drizzle-kit push
```

Start the app:

```sh
pnpm dev
```

## API

```sh
curl -X POST http://localhost:3000/api/runs \
  -H "Content-Type: application/json" \
  -d '{"model": "Claude 3.7 Sonnet"}'

curl -X POST http://localhost:3000/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "runId": 1,
    "language": "javascript",
    "exercise": "binary",
    "tokensIn": 1000000,
    "tokensOut": 50000,
    "tokensContext": 50,
    "cacheWrites": 5,
    "cacheReads": 10,
    "cost": 0.543,
    "duration": 150000,
    "passed": true
  }'
```
