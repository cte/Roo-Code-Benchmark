import { desc } from 'drizzle-orm';

import { db, runs as runsTable } from '@/db';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui';

export const dynamic = 'force-dynamic';

export default async function Home() {
  const runs = await db.select().from(runsTable).orderBy(desc(runsTable.createdAt));

  return (
    <div className="mx-auto my-20 w-3xl">
      <Table className="border">
        <TableHeader>
          <TableRow>
            <TableHead>ID</TableHead>
            <TableHead>Timestamp</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {runs.map(run => (
            <TableRow key={run.id}>
              <TableCell>{run.id}</TableCell>
              <TableCell>{new Date(run.createdAt).toLocaleString()}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
