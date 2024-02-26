import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import { GET } from '@/app/api/request';
import ContentFiltering from '@/app/components/ContentFiltering';
import { getServerSession } from 'next-auth/next';

export default async function Page() {
  // Get Session
  const session = await getServerSession(authOptions);
  console.log(session.user.token);

  // Get Data
  const url = 'api/user/' + session.user.username + '/games';
  const data = await GET(url, session.user.token);

  return <ContentFiltering data={data} />;
}
