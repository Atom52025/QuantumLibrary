import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import UserMenu from '@/app/components/user/UserInfo';
import { getServerSession } from 'next-auth/next';
import {GET} from "@/app/api/tokenRequest";
import StatSection from "@/app/components/sections/StatSection";

export default async function Page() {
  // Get Session
  const session = await getServerSession(authOptions);

  // Get Games Data
  const url = 'api/user/' + session.user.username + '/games/stats';
  const data = await GET(url, session.user.token);

  // Get Groups Data
  const urlGroups = 'api/user/' + session.user.username + '/groups';
  const dataGroups = await GET(urlGroups, session.user.token);

  return <StatSection data={data} gData={dataGroups || { accepted: [], pending: [] }} />;
}
