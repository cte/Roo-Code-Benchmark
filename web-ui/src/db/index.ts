import 'dotenv/config';
import { drizzle } from 'drizzle-orm/libsql';

export * from './schema';

export const db = drizzle({ connection: { url: process.env.DB_FILE_NAME! } });
