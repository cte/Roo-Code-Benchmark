import { getRuns } from '@/db/queries';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui';

export const dynamic = 'force-dynamic';

const formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
});

export default async function Home() {
  const runs = await getRuns();

  return (
    <div className="mx-auto my-20 w-3xl">
      <Table className="border">
        <TableHeader>
          <TableRow>
            <TableHead>ID</TableHead>
            <TableHead>Model</TableHead>
            <TableHead>Timestamp</TableHead>
            <TableHead>Passed</TableHead>
            <TableHead>Failed</TableHead>
            <TableHead>% Correct</TableHead>
            <TableHead>Cost</TableHead>
            <TableHead>Duration</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {runs.map(run => (
            <TableRow key={run.id}>
              <TableCell>{run.id}</TableCell>
              <TableCell>{run.model}</TableCell>
              <TableCell>{new Date(run.createdAt).toLocaleString()}</TableCell>
              <TableCell>{run.passed}</TableCell>
              <TableCell>{run.failed}</TableCell>
              <TableCell>{(run.rate * 100).toFixed(1)}%</TableCell>
              <TableCell>{formatter.format(run.cost)}</TableCell>
              <TableCell>{Math.round(run.duration / 1000)}s</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
