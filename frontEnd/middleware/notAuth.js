export default function ({ store, redirect }) 
{
  if (store.state.user) {
    return redirect(store.state.user.role.default_path)
  }
}
