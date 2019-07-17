const StringUtils = {
  getInitials: (...names) => {
    return names.map((name) =>{
      let initials = name.match(/\b\w/g) || [];
      return initials.join('');
    }).join('').toUpperCase();
  }
}

export default StringUtils;
