import { GET } from '@/app/api/request';
import ContentFiltering from '@/app/components/ContentDisplay';

export default async function Page({ params }) {
  // Get Data
  const url = 'api/games';
  const data = await GET(url);

  return <ContentFiltering data={data ? data.games : []} />;
}
