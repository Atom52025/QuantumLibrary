import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import UserMenu from '@/app/components/user/UserInfo';
import { getServerSession } from 'next-auth/next';

export default async function Page() {
  return <UserMenu />;
}
