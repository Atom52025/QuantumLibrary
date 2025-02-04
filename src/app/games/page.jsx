import { GET } from '@/app/api/request';
import ContentDisplay from '@/app/components/non-user/ContentDisplay';

export default async function Page({ params }) {
  // Get Data
  const url = 'api/games';
  const data = await GET(url);

  return <ContentDisplay data={data ? data.games : []} />;
}
