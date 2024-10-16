import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import { GET } from '@/app/api/tokenRequest';
import ContentFiltering from '@/app/components/non-user/ContentDisplay';
import GroupInfoSection from '@/app/components/sections/GroupInfoSection';
import { getServerSession } from 'next-auth/next';

export default async function Page({ params }) {
  // Get Session
  const session = await getServerSession(authOptions);

  // Get Group Data
  const url = 'api/groups/' + params.groupId;
  const data = await GET(url, session.user.token);

  // Get Groups Data
  const urlGroups = 'api/user/' + session.user.username + '/groups';
  const dataGroups = await GET(urlGroups, session.user.token);

  return (
    <>
      <GroupInfoSection group={data?.group} />
      <ContentFiltering data={data?.games} gData={dataGroups || { accepted: [], pending: [] }} />
    </>
  );
}
