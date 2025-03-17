import { desc, eq, sql } from 'drizzle-orm';

import { db, runs, tasks } from '@/db';

export const getRuns = () =>
  db
    .select({
      id: runs.id,
      model: runs.model,
      description: runs.description,
      createdAt: runs.createdAt,
      passed: sql<number>`sum(${tasks.passed})`,
      failed: sql<number>`sum(${tasks.passed} = 0)`,
      total: sql<number>`count(${tasks.id})`,
      rate: sql<number>`sum(${tasks.passed}) * 1.0 / count(${tasks.id})`,
      cost: sql<number>`sum(${tasks.cost})`,
      duration: sql<number>`sum(${tasks.duration})`,
    })
    .from(runs)
    .leftJoin(tasks, eq(runs.id, tasks.runId))
    .orderBy(desc(runs.id));
