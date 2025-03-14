import { NextResponse } from 'next/server';

import { db, tasks, insertTaskSchema } from '@/db';

export async function POST(request: Request) {
  try {
    const task = await db
      .insert(tasks)
      .values({
        ...insertTaskSchema.parse(await request.json()),
        createdAt: new Date(),
      })
      .returning();

    return NextResponse.json({ task }, { status: 201 });
  } catch (error) {
    return NextResponse.json(
      { error: (error as Error).message },
      { status: 500 },
    );
  }
}
