import { int, sqliteTable, text } from 'drizzle-orm/sqlite-core';
import * as t from 'drizzle-orm/sqlite-core';

export const runs = sqliteTable(
  'runs',
  {
    id: int().primaryKey({ autoIncrement: true }),
    key: text().notNull(),
    createdAt: int().notNull(),
  },
  table => [t.uniqueIndex('key_idx').on(table.key)],
);
