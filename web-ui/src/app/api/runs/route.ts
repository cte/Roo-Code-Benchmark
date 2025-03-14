import { NextResponse } from 'next/server';
import { eq } from 'drizzle-orm';
import { createInsertSchema } from 'drizzle-zod';

import { db, runs } from '@/db';

const insertRunSchema = createInsertSchema(runs).omit({
  id: true,
  createdAt: true,
});

export async function POST(request: Request) {
  try {
    const payload = insertRunSchema.parse(await request.json());

    const run = await db.insert(runs).values({
      ...payload,
      createdAt: Date.now(),
    }).returning();


    return NextResponse.json({ run }, { status: 201 });
  } catch (error) {
    return NextResponse.json(
      {
        success: false,
        message: 'Failed to create run',
        error: (error as Error).message,
      },
      { status: 500 },
    );
  }
}
