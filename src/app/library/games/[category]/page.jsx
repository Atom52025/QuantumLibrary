import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import { GET } from '@/app/api/tokenRequest';
import UserContentDisplay from '@/app/components/user/UserContentDisplay';
import { getServerSession } from 'next-auth/next';

export default async function Page({ params }) {
  // Get Session
  const session = await getServerSession(authOptions);

  // Get Games Data
  const url = 'api/user/games?category=' + params.category;
  const data = await GET(url, session.user.token, ['games']);

  // Get Groups Data
  const urlGroups = 'api/user/groups';
  const dataGroups = await GET(urlGroups, session.user.token);

  return <UserContentDisplay data={data.games} gData={dataGroups || { accepted: [], pending: [] }} />;
}
