import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import { GET } from '@/app/api/tokenRequest';
import GroupInfoSection from '@/app/components/sections/GroupInfoSection';
import GroupContentDisplay from '@/app/components/user/GroupContentDisplay';
import { getServerSession } from 'next-auth/next';

export default async function Page({ params }) {
  // Get Session
  const session = await getServerSession(authOptions);

  // Get Group Data
  const url = 'api/group/' + params.groupId;
  const data = await GET(url, session.user.token);

  // Get Groups Data
  const urlGroups = 'api/user/groups';
  const dataGroups = await GET(urlGroups, session.user.token);

  return (
    <>
      <GroupInfoSection group={data?.group} />
      <GroupContentDisplay data={data?.games} group={data?.group} gData={dataGroups || { accepted: [], pending: [] }} />
    </>
  );
}
