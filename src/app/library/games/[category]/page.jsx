import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import { GET } from '@/app/api/tokenRequest';
import UserContentFiltering from '@/app/components/UserContentFiltering';
import { getServerSession } from 'next-auth/next';

export default async function Page({ params }) {
  // Get Session
  const session = await getServerSession(authOptions);

  // Get Data
  const url = 'api/user/' + session.user.username + '/games?category=' + params.category;
  const data = await GET(url, session.user.token);

  return <UserContentFiltering data={data ? data.games : []} />;
}
