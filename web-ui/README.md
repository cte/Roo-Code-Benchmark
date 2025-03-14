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
curl -X POST \
  http://localhost:3000/api/runs \
  -H "Content-Type: application/json" \
  -d '{"key": "test-key"}'
```
